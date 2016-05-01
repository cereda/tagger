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

import br.usp.poli.lta.cereda.tagger.model.Publication;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Fornece métodos utilitários.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Utils {

    /**
     * Exibe mensagem.
     * @param title Título.
     * @param text Texto.
     */
    public static void printMessage(String title, String text) {
        System.out.println(StringUtils.rightPad(title.toUpperCase().concat(" "), 70, '-'));
        System.out.println(WordUtils.wrap(text, 70, "\n", true));
        System.out.println(StringUtils.repeat('-', 70));
    }
    
    /**
     * Exibe a publicação.
     * @param publication Publicação.
     */
    public static void printPublication(Publication publication) {
        System.out.println(StringUtils.repeat('-', 70));
        System.out.println(StringUtils.center(Messages.getMessage("DISPLAY_TITLE").toUpperCase(), 70, ' '));
        System.out.println(StringUtils.repeat('-', 70));
        System.out.println(publication);
        System.out.println(StringUtils.repeat('-', 70));
    }

    /**
     * Exibe exceção.
     * @param exception Exceção.
     * @param error Erro a ser retornado.
     */
    public static void printException(Exception exception, int error) {
        printMessage(Messages.getMessage("EXCEPTION_THROWN"), exception.getMessage());
        System.exit(error);
    }

    /**
     * Calcula o hash do arquivo.
     * @param file Arquivo.
     * @return Valor longo referente ao hash do arquivo.
     * @throws IOException Exceção de entrada e saída.
     */
    public static long calculateChecksum(File file) throws IOException {
        return FileUtils.checksumCRC32(file);
    }

    /**
     * Transforma o texto informado em um conjunto.
     * @param text Texto.
     * @return Conjunto.
     */
    public static Set<String> toSet(String text) {
        return Arrays.asList(text.split(";")).stream().map(String::trim).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Exibe relatório.
     * @param title Título.
     * @param lines Linhas.
     */
    public static void printReport(String title, List<String> lines) {
        System.out.println(StringUtils.rightPad(title.toUpperCase().concat(" "), 70, '-'));
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
            if (i < lines.size() - 1) {
                System.out.println(StringUtils.repeat('=', 70));
            }
        }
        System.out.println(StringUtils.repeat('-', 70));
    }

    /**
     * Obtém as opções de linha de comando.
     * @return Opções de linha de comando.
     */
    public static Options getOptions() {
        Options options = new Options();

        options.addOption("e", "entry", true, Messages.getMessage("OPT_ENTRY"));
        options.addOption("h", "help", false, Messages.getMessage("OPT_HELP"));
        options.addOption("d", "database", true, Messages.getMessage("OPT_DATABASE"));
        options.addOption("u", "update", false, Messages.getMessage("OPT_UPDATE"));
        options.addOption("r", "remove", false, Messages.getMessage("OPT_REMOVE"));
        options.addOption("s", "search", false, Messages.getMessage("OPT_SEARCH"));
        options.addOption("t", "tags", true, Messages.getMessage("OPT_TAGS"));
        options.addOption("a", "authors", true, Messages.getMessage("OPT_AUTHORS"));
        options.addOption("D", "display", false, Messages.getMessage("OPT_DISPLAY"));

        return options;
    }

    /**
     * Define o leiaute padrão do sistema.
     */
    public static void setupUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            // quack
        }
    }

    /**
     * Exibe uma janela de entrada de dados.
     * @param width Largura da janela.
     * @param title Título.
     * @param text Texto.
     * @param input Valor da entrada.
     * @return Valor opcional contendo um texto.
     */
    public static Optional<String> showInputBox(int width, String title, String text, String input) {
        Object result = JOptionPane.showInputDialog(null, String.format("<html><body style=\"width:%dpx\">%s</body></html>", width, text), title, JOptionPane.QUESTION_MESSAGE, null, null, input);
        return (result == null ? Optional.empty() : Optional.of(result.toString()));
    }

    /**
     * Constrói entradas a partir de um conjunto de publicações.
     * @param query Conjunto de publicações.
     * @return Lista de textos.
     */
    public static List<String> buildEntries(Set<Publication> query) {
        return query.stream().map(Publication::toString).collect(Collectors.toList());
    }

    /**
     * Assegura que o arquivo existe.
     * @param file Arquivo.
     * @throws Exception Arquivo não existe.
     */
    public static void ensureFileExists(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception(Messages.getMessage("FILE_DOES_NOT_EXIST", file.getAbsolutePath()));
        }
    }

    /**
     * Assegura que o arquivo é um arquivo de fato.
     * @param file Arquivo.
     * @throws Exception O arquivo não é um arquivo.
     */
    public static void ensureFile(File file) throws Exception {
        if (!file.isFile()) {
            throw new Exception(Messages.getMessage("FILE_IS_NOT_FILE", file.getAbsolutePath()));
        }
    }

    /**
     * Assegura que o arquivo é um diretório.
     * @param file Arquivo.
     * @throws Exception O arquivo não é um diretório.
     */
    public static void ensureDirectory(File file) throws Exception {
        if (!file.isDirectory()) {
            throw new Exception(Messages.getMessage("FILE_IS_NOT_DIRECTORY", file.getAbsolutePath()));
        }
    }

    /**
     * Assegura que a consulta não é vazia.
     * @param query Consulta.
     * @throws Exception A consulta é vazia.
     */
    public static void ensureQuery(Set<String> query) throws Exception {
        if (query.stream().filter((String t) -> t.trim().isEmpty()).count() == 0) {
            throw new Exception(Messages.getMessage("INVALID_QUERY"));
        }
    }

    /**
     * Exibe o logotipo do programa.
     */
    public static void printBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("  _                          ").append('\n');
        sb.append(" | |_ __ _ __ _ __ _ ___ _ _ ").append('\n');
        sb.append(" |  _/ _` / _` / _` / -_) '_|").append('\n');
        sb.append("  \\__\\__,_\\__, \\__, \\___|_|  ").append('\n');
        sb.append("          |___/|___/         ").append('\n').append('\n');
        sb.append("tagger 1.0 -- Etiquetador de publicações").append('\n');
        sb.append("Laboratório de Linguagens e Técnicas Adaptativas").append('\n');
        sb.append("Escola Politécnica, Universidade de São Paulo").append('\n');
        System.out.println(sb.toString());
    }

}
