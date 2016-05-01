/**
 * ------------------------------------------------------
 *    Laboratório de Linguagens e Técnicas Adaptativas
 *       Escola Politécnica, Universidade São Paulo
 * ------------------------------------------------------
 * 
 * Copyright (c) 2016, Paulo Roberto Massa Cereda
 * 
 * Permission  is  hereby  granted, free  of  charge,  to
 * any  person  obtaining a  copy  of  this software  and
 * associated  documentation files  (the "Software"),  to
 * deal  in the  Software without  restriction, including
 * without limitation  the rights  to use,  copy, modify,
 * merge,  publish, distribute,  sublicense, and/or  sell
 * copies of the Software, and  to permit persons to whom
 * the Software  is furnished  to do  so, subject  to the
 * following conditions:
 * 
 * The above copyright notice  and this permission notice
 * shall  be  included  in   all  copies  or  substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED  "AS IS", WITHOUT WARRANTY OF
 * ANY  KIND,  EXPRESS  OR  IMPLIED,  INCLUDING  BUT  NOT
 * LIMITED TO THE  WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A  PARTICULAR PURPOSE  AND NONINFRINGEMENT.  IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES  OR OTHER LIABILITY, WHETHER IN
 * AN  ACTION OF  CONTRACT,  TORT  OR OTHERWISE,  ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.usp.poli.lta.cereda.tagger.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Implementa as mensagens do programa.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Messages {

    private static final ResourceBundle RB = ResourceBundle.getBundle("br.usp.poli.lta.cereda.tagger.messages.Messages");

    /**
     * Construtor privado.
     */
    private Messages() {
        // quack
    }

    /**
     * Obtém a mensagem de acordo com a chave informada.
     * @param key Chave.
     * @return Mensagem de acordo com a chave informada.
     */
    public static String getMessage(String key) {
        try {
            return RB.getString(key);
        } catch (MissingResourceException exception) {
            return "NOT FOUND: ".concat(key);
        }
    }

    /**
     * Obtém a mensagem de acordo com a chave informada, formatada com os
     * argumentos informados.
     * @param key Chave.
     * @param parameters Argumentos da mensagem.
     * @return Mensagem de acordo com a chave informada, formatada com os
     * argumentos informados.
     */
    public static String getMessage(String key, Object... parameters) {
        try {
            return MessageFormat.format(RB.getString(key), parameters);
        } catch (MissingResourceException exception) {
            return "NOT FOUND: ".concat(key);
        }
    }

}
