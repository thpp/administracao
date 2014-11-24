package br.com.administracao.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
	//private static Logger logger = Logger.getLogger(Helper.class.getName());
	
    public String retiraMascara(String pValue){
        Pattern p = Pattern.compile("\\/|\\.|-");
        Matcher m = p.matcher(pValue);
        pValue = m.replaceAll("");
        return  pValue;
    }
    
    public String aplicarMascara(String pValue) {

        String pMask = "";

        if (pValue.length() > 11) {
            pMask = "##.###.###/####-##";
        } else {
            pMask = "###.###.###-##";
        }

        /*  
         * Formata valor com a mascara passada   
         */
        for (int i = 0; i < pValue.length(); i++) {
            pMask = pMask.replaceFirst("#", pValue.substring(i, i + 1));
        }

        /*  
         * Subistitui por string vazia os digitos restantes da mascara  
         * quando o valor passado Ã© menor que a mascara    
         */
        return pMask.replaceAll("#", "");
    }

    public String convertCalendarToString(String formato, java.sql.Date data) {

        Date novaData = new java.util.Date(data.getTime());

        // cria o formatador  
        SimpleDateFormat formatador = new SimpleDateFormat(formato);

        return formatador.format(novaData);

    }	
	
	public String retiraAcentuacoes(String entrada) {

        String[] replaces = {"a", "e", "i", "o", "u", "c"};

        Pattern[] Patterns = new Pattern[replaces.length];

        Patterns[0] = Pattern.compile("[Ã¢Ã£Ã¡Ã Ã¤]", Pattern.CASE_INSENSITIVE);
        Patterns[1] = Pattern.compile("[Ã©Ã¨ÃªÃ«]", Pattern.CASE_INSENSITIVE);
        Patterns[2] = Pattern.compile("[Ã­Ã¬Ã®Ã¯]", Pattern.CASE_INSENSITIVE);
        Patterns[3] = Pattern.compile("[Ã³Ã²Ã´ÃµÃ¶]", Pattern.CASE_INSENSITIVE);
        Patterns[4] = Pattern.compile("[ÃºÃ¹Ã»Ã¼]", Pattern.CASE_INSENSITIVE);
        Patterns[5] = Pattern.compile("[Ã§]", Pattern.CASE_INSENSITIVE);

        String result = entrada;

        for (int i = 0; i < Patterns.length; i++) {

            Matcher matcher = Patterns[i].matcher(result);
            result = matcher.replaceAll(replaces[i]);

        }

        return result;
    }
	
	public boolean validaCnpj(String str_cnpj) {
        if (!str_cnpj.substring(0, 1).equals("")) {
            try {
                str_cnpj = str_cnpj.replace('.', ' ');
                str_cnpj = str_cnpj.replace('/', ' ');
                str_cnpj = str_cnpj.replace('-', ' ');
                str_cnpj = str_cnpj.replaceAll(" ", "");
                int soma = 0, aux, dig;
                String cnpj_calc = str_cnpj.substring(0, 12);

                if (str_cnpj.length() != 14) {
                    return false;
                }
                char[] chr_cnpj = str_cnpj.toCharArray();
                /* Primeira parte */
                for (int i = 0; i < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                /* Segunda parte */
                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                return str_cnpj.equals(cnpj_calc);
            } catch (Exception e) {
                System.err.println("Erro !" + e);
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean validacpf(String strCpf) { // formato XXX.XXX.XXX-XX  
        if (!strCpf.substring(0, 1).equals("")) {
            try {
                boolean validado = true;
                int d1, d2;
                int digito1, digito2, resto;
                int digitoCPF;
                String nDigResult;
                strCpf = strCpf.replace('.', ' ');
                strCpf = strCpf.replace('-', ' ');
                strCpf = strCpf.replaceAll(" ", "");
                d1 = d2 = 0;
                digito1 = digito2 = resto = 0;

                for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
                    digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount)).intValue();

                    //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.  
                    d1 = d1 + (11 - nCount) * digitoCPF;

                    //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.  
                    d2 = d2 + (12 - nCount) * digitoCPF;
                };

                //Primeiro resto da divisÃ£o por 11.  
                resto = (d1 % 11);

                //Se o resultado for 0 ou 1 o digito Ã© 0 caso contrÃ¡rio o digito Ã© 11 menos o resultado anterior.  
                if (resto < 2) {
                    digito1 = 0;
                } else {
                    digito1 = 11 - resto;
                }

                d2 += 2 * digito1;

                //Segundo resto da divisÃ£o por 11.  
                resto = (d2 % 11);

                //Se o resultado for 0 ou 1 o digito Ã© 0 caso contrÃ¡rio o digito Ã© 11 menos o resultado anterior.  
                if (resto < 2) {
                    digito2 = 0;
                } else {
                    digito2 = 11 - resto;
                }

                //Digito verificador do CPF que estÃ¡ sendo validado.  
                String nDigVerific = strCpf.substring(strCpf.length() - 2, strCpf.length());

                //Concatenando o primeiro resto com o segundo.  
                nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

                //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
                return nDigVerific.equals(nDigResult);
            } catch (Exception e) {
                System.err.println("Erro !" + e);
                return false;
            }
        } else {
            return false;
        }
    }

}
