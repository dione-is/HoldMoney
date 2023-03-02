package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.dto.MensagemDTO;
import com.diotech.minhasfinancas.dto.SmsDTO;
import com.diotech.minhasfinancas.entity.*;
import com.diotech.minhasfinancas.enums.MensagemStatus;
import com.diotech.minhasfinancas.enums.TipoLancamento;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.repository.MensagemExternaLoteRepository;
import com.diotech.minhasfinancas.repository.MensagemExternaRepository;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import com.diotech.minhasfinancas.service.UsuarioService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private MensagemExternaLoteRepository mensagemExternaLoteRepository;

    @Autowired
    private MensagemExternaRepository mensagemExternaRepository;

    private TipoMensagemExterna tipoMensagemExterna;

    private final Logger logger = LoggerFactory.getLogger(String.class);

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmailAndSenha(email, senha);
        if (usuario.isPresent()) {
            return usuario.get();
        }
        throw new ErroAutenticacao("Email ou Senha Invalido");
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    private void validarUsuario(Usuario usuario) {
        if(usuario.getNome() == null || "".equals(usuario.getNome().trim())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuario deve ser informado");
        }
        if(usuario.getEmail() == null || "".equals(usuario.getEmail().trim() )){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Email do usuario deve ser informado");
        }
        if(usuario.getSenha() == null || "".equals(usuario.getSenha().trim())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ser informado");
        }
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("já existe esse email Cadastrado");
        }
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public BigDecimal BuscarSaldoUsuario(Long id) {

        BigDecimal receita = repository.buscarSaldoPorUsuarioAndTipoLancamento(id, TipoLancamento.RECEITA.toString());
        BigDecimal despesa = repository.buscarSaldoPorUsuarioAndTipoLancamento(id, TipoLancamento.DESPESA.toString());
        receita = receita == null ? BigDecimal.ZERO : receita;
        return receita.subtract(despesa == null ? BigDecimal.ZERO : despesa);
    }

    @Override
    public void enviarSmsParaTodos(MensagemDTO mensagem) {
        if (mensagem != null || mensagem.getMensagem().trim() != "") {
            List<Usuario> usuarioList = repository.findAll();

            this.tipoMensagemExterna = new TipoMensagemExterna();
            this.tipoMensagemExterna = mensagem.getTipoMensagemExterna();

            usuarioList.stream().forEach(e -> {
                MensagemExternaLote mensagemExternaLote = new MensagemExternaLote();
                MensagemExterna mensagemExterna = new MensagemExterna();
                mensagemExterna.setAssunto(mensagem.getAssunto());
                mensagemExterna.setMensagem(mensagem.getMensagem());
                mensagemExterna.setContato(e.getTelefone());
                mensagemExterna.setPessoa(e);
                mensagemExternaLote.setDataEmissao(new Date());
                mensagemExternaLote.setQuantidadeEnviado(usuarioList.size());
                mensagemExternaLote.setMensagemExterna(mensagemExternaRepository.save(mensagemExterna));
                MensagemExternaStatus mensagemExternaStatus = new MensagemExternaStatus();
                mensagemExternaStatus.setData(new Date());
                mensagemExternaLote.setMensagemExternaStatus(mensagemExternaStatus);
                mensagemExternaLote.setTipoMensagemExterna(tipoMensagemExterna);
                mensagemExternaLote = mensagemExternaLoteRepository.save(mensagemExternaLote);

                Client client = ClientBuilder.newClient();
                String url = "https://mex10.com/api/shortcodev2.aspx?token=4b813225f16b055bbbf1088f284dff949547e39501eeebaf34bcd81dde471706&t=send&n=" +
                        e.getTelefone() + "&m=" + mensagem.getMensagem();
                Response response = client.target(url)
                        .request(MediaType.TEXT_PLAIN_TYPE)
                        .get();
                if (response.getStatus() != 200) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao enviar sms");
                }
                mensagemExternaStatus.setMensagemStatus(MensagemStatus.ENVIADO);
                mensagemExternaLoteRepository.save(mensagemExternaLote);
            });
        }
    }

    @Override
    public void enviarSmsUsuario(SmsDTO sms) {


        if (validarSms(sms)) {
            Client client = ClientBuilder.newClient();
            String url = "https://mex10.com/api/shortcodev2.aspx?token=4b813225f16b055bbbf1088f284dff949547e39501eeebaf34bcd81dde471706&t=send&n=" +
                    sms.getUsuario().getTelefone() + "&m=" + sms.getMensagem();
            Response response = client.target(url)
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .get();
            if (response.getStatus() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao foi possivel enviar mensagem, entre em contato com o suporte");
            }
        }
    }

    public boolean validarSms(SmsDTO sms) {
        if (sms.getUsuario().getTelefone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado o telefone do destinatario");
        }
        if (sms.getMensagem() == null || sms.getMensagem().trim() == "") {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado a mensagem de texto para o sms");
        }
        return true;
    }

    @Override
    public void enviarWhatsAppUsuario(SmsDTO sms) throws IOException {

        if (validarWpp(sms)) {
            OkHttpClient client = new OkHttpClient();
            com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"phone\": \"" + sms.getUsuario().getTelefone() + "\", \"message\": \"" + sms.getMensagem() + "\"}");
            Request request = new Request.Builder()
                    .url("https://api.z-api.io/instances/3B8EBD68660BD08B68510A394DBBCDD9/token/B8276A685578649E63C923E9/send-messages")
                    .post(body)
                    .build();

            com.squareup.okhttp.Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "falha ao enviar mensagem, entre em contato com o suporte");
            }
        }
    }

    private boolean validarWpp(SmsDTO sms) {
        if (sms.getMensagem() == null || sms.getMensagem().trim() == "") {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser Informado uma mensagem");
        }
        if (sms.getUsuario().getTelefone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario deve possuir um telefone cadastrado");
        }
        return true;
    }


    public TipoMensagemExterna getTipoMensagemExterna() {
        return tipoMensagemExterna;
    }

    public void setTipoMensagemExterna(TipoMensagemExterna tipoMensagemExterna) {
        this.tipoMensagemExterna = tipoMensagemExterna;
    }


    @Override
    public void enviarMensagem(MensagemDTO mensagemDTO) {
        if (mensagemDTO != null || mensagemDTO.getMensagem().trim() != "") {
            List<Usuario> usuarioList = repository.findAll();

            this.tipoMensagemExterna = new TipoMensagemExterna();
            this.tipoMensagemExterna = mensagemDTO.getTipoMensagemExterna();

            usuarioList.stream().forEach(e -> {
                MensagemExternaLote mensagemExternaLote = new MensagemExternaLote();
                MensagemExterna mensagemExterna = new MensagemExterna();
                mensagemExterna.setAssunto(mensagemDTO.getAssunto());
                mensagemExterna.setMensagem(mensagemDTO.getMensagem());
                mensagemExterna.setContato(e.getTelefone());
                mensagemExterna.setPessoa(e);
                mensagemExternaLote.setDataEmissao(new Date());
                mensagemExternaLote.setQuantidadeEnviado(usuarioList.size());
                mensagemExternaLote.setMensagemExterna(mensagemExternaRepository.save(mensagemExterna));
                MensagemExternaStatus mensagemExternaStatus = new MensagemExternaStatus();
                mensagemExternaStatus.setData(new Date());
                mensagemExternaLote.setMensagemExternaStatus(mensagemExternaStatus);
                mensagemExternaLote.setTipoMensagemExterna(tipoMensagemExterna);
                mensagemExternaLote = mensagemExternaLoteRepository.save(mensagemExternaLote);

                if (mensagemDTO.getTipoMensagemExterna().getDescricao().contains("SMS")) {
                    Client client = ClientBuilder.newClient();
                    String url = "https://mex10.com/api/shortcodev2.aspx?token=4b813225f16b055bbbf1088f284dff949547e39501eeebaf34bcd81dde471706&t=send&n=" +
                            e.getTelefone() + "&m=" + mensagemDTO.getMensagem();
                    Response response = client.target(url)
                            .request(MediaType.TEXT_PLAIN_TYPE)
                            .get();
                    if (response.getStatus() != 200) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao enviar sms");
                    }
                    mensagemExternaStatus.setMensagemStatus(MensagemStatus.ENVIADO);
                    mensagemExternaLoteRepository.save(mensagemExternaLote);
                } else if (mensagemDTO.getMensagem().contains("WhatsApp")) {
                    OkHttpClient client = new OkHttpClient();
                    com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\"phone\": \"55" + e.getTelefone() + "\", \"message\": \"" + mensagemDTO.getMensagem() + "\"}");
                    Request request = new Request.Builder()
                            .url("https://api.z-api.io/instances/3B8EBD68660BD08B68510A394DBBCDD9/token/B8276A685578649E63C923E9/send-messages")
                            .post(body)
                            .build();

                    com.squareup.okhttp.Response response = null;

                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (response.code() != 200) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.message());
                    }
                    mensagemExternaStatus.setMensagemStatus(MensagemStatus.ENVIADO);
                    mensagemExternaLoteRepository.save(mensagemExternaLote);

                } else if (mensagemDTO.getTipoMensagemExterna().getDescricao().contains("E-mail")) {
                    String emailRemetente = "comercial@wgautomacao.com.br";
                    String nomeRemetente = "WG Automacao";
                    String assunto = mensagemDTO.getAssunto();
                    String body = mensagemDTO.getMensagem();

                    String protocolo = "smtp";
                    String servidor = "smtplw.com.br";  // do painel de controle do SMTP
                    String username = "josemariawg"; // do painel de controle do SMTP
                    String senha = "bPVGyjWt2120"; // do painel de controle do SMTP
                    String porta = "587";   // do painel de controle do SMTP

                    Properties props = new Properties();
                    props.put("mail.transport.protocol", protocolo);
                    props.put("mail.smtp.host", servidor);
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", porta);

                    Session session = Session.getDefaultInstance(props, null);
                    session.setDebug(false);

                    try {
                        InternetAddress iaFrom = new InternetAddress(emailRemetente, nomeRemetente);
                        InternetAddress[] iaTo = new InternetAddress[1];
                        InternetAddress[] iaReplyTo = new InternetAddress[1];

                        iaReplyTo[0] = new InternetAddress(e.getEmail(), e.getNome());
                        iaTo[0] = new InternetAddress(e.getEmail(), e.getNome());

                        MimeMessage msg = new MimeMessage(session);

                        if (iaReplyTo != null)
                            msg.setReplyTo(iaReplyTo);
                        if (iaFrom != null)
                            msg.setFrom(iaFrom);
                        if (iaTo.length > 0)
                            msg.setRecipients(Message.RecipientType.TO, iaTo);
                        msg.setSubject(assunto);
                        msg.setSentDate(new Date());

                        msg.setContent(body, "text/html");
                        msg.setHeader("x-auth-token", "747be58fab73a650fdcc024f13141b97");
                        Transport tr = session.getTransport(protocolo);
                        tr.connect(servidor, username, senha);

                        msg.saveChanges();

                        tr.sendMessage(msg, msg.getAllRecipients());
                        tr.close();

                        mensagemExternaStatus.setMensagemStatus(MensagemStatus.ENVIADO);
                        mensagemExternaLoteRepository.save(mensagemExternaLote);
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    public HttpStatus atualizarStatusMensagemWhatsApp(LinkedHashMap webHookWhatsApp) {
        WebHookWhatsApp novo = new WebHookWhatsApp();
        String idExterno =  webHookWhatsApp.get("ids").toString().replace("[","").replace("]","");
        String status = webHookWhatsApp.get("status").toString();
        logger.error(status);
        logger.error(idExterno);
        return HttpStatus.OK;
    }


}
