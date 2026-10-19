package br.com.acta.common.utils;

import br.com.caelum.stella.format.CPFFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class Formatador {
    private final CPFFormatter CPF_FORMATTER = new CPFFormatter();
    private final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    private boolean ehInvalido(String valor){
        return valor == null || valor.isBlank();
    }

    @Named("formatarCpf")
    public String formatarCpf(String cpf){
        if (ehInvalido(cpf)) return null;
        return CPF_FORMATTER.format(cpf);
    }

    @Named("formatarTelefone")
    public String formatarTelefone(String telefone) throws NumberParseException {
        if (ehInvalido(telefone)) return null;
        Phonenumber.PhoneNumber numero = PHONE_NUMBER_UTIL.parse(telefone, "BR");
        return PHONE_NUMBER_UTIL.format(numero, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
    }
}