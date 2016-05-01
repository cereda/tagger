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
package br.usp.poli.lta.cereda.tagger;

import br.usp.poli.lta.cereda.tagger.utils.Utils;
import br.usp.poli.lta.cereda.tagger.model.Database;
import br.usp.poli.lta.cereda.tagger.model.Publication;
import br.usp.poli.lta.cereda.tagger.utils.Messages;
import java.io.File;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Classe principal.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Application {

    /**
     * Executa o método principal.
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {

        Utils.setupUI();
        Utils.printBanner();

        Options options = Utils.getOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("help")) {
                throw new ParseException("quack");
            }

            if (arguments(line, "display", "entry")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                Database database = new Database(entry, false);
                Publication publication = database.getSinglePublication();
                publication.sanitize();

                Utils.printPublication(publication);
                System.exit(0);

            }

            if (arguments(line, "display", "entry", "database")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);
                Publication publication = database.fromPDFtoPublication(entry);
                publication.sanitize();

                Utils.printPublication(publication);
                System.exit(0);

            }

            if (arguments(line, "entry", "update")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                Database database = new Database(entry, false);
                Publication publication = database.getSinglePublication();

                Optional<String> title = Utils.showInputBox(250, Messages.getMessage("BOX_TITLE_TITLE"), Messages.getMessage("BOX_TITLE_MESSAGE"), publication.getTitle());
                Optional<String> authors = Utils.showInputBox(250, Messages.getMessage("BOX_AUTHORS_TITLE"), Messages.getMessage("BOX_AUTHORS_MESSAGE"), publication.getFlattenedAuthors());
                Optional<String> tags = Utils.showInputBox(250, Messages.getMessage("BOX_TAGS_TITLE"), Messages.getMessage("BOX_TAGS_MESSAGE"), publication.getFlattenedTags());

                if (!validate(title, authors, tags)) {
                    throw new Exception(Messages.getMessage("DO_NOT_LEAVE_EMPTY_FIELDS"));
                }

                publication.setTitle(title.get());
                publication.setAuthorsFromString(authors.get());
                publication.setTagsFromString(tags.get());
                publication.sanitize();

                database.update(publication);
                if (!database.updatePDF(entry)) {
                    throw new Exception(Messages.getMessage("PDF_UPDATE_ERROR", entry.getAbsolutePath()));
                }

                Utils.printMessage(Messages.getMessage("UPDATE_TITLE"), Messages.getMessage("UPDATE_MESSAGE"));
                System.exit(0);
            }

            if (arguments(line, "entry", "update", "database")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                File xml = new File(line.getOptionValue("database"));
                
                Database database = new Database(xml, true);
                Publication publication = database.fromPDFtoPublication(entry);

                Optional<String> title = Utils.showInputBox(250, Messages.getMessage("BOX_TITLE_TITLE"), Messages.getMessage("BOX_TITLE_MESSAGE"), publication.getTitle());
                Optional<String> authors = Utils.showInputBox(250, Messages.getMessage("BOX_AUTHORS_TITLE"), Messages.getMessage("BOX_AUTHORS_MESSAGE"), publication.getFlattenedAuthors());
                Optional<String> tags = Utils.showInputBox(250, Messages.getMessage("BOX_TAGS_TITLE"), Messages.getMessage("BOX_TAGS_MESSAGE"), publication.getFlattenedTags());

                if (!validate(title, authors, tags)) {
                    throw new Exception(Messages.getMessage("DO_NOT_LEAVE_EMPTY_FIELDS"));
                }

                publication.setTitle(title.get());
                publication.setAuthorsFromString(authors.get());
                publication.setTagsFromString(tags.get());
                publication.sanitize();

                database.update(publication);

                Utils.printMessage(Messages.getMessage("UPDATE_TITLE"), Messages.getMessage("UPDATE_MESSAGE"));
                System.exit(0);
            }

            if (arguments(line, "entry", "remove")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                if (!Database.removeDataFromPDF(entry)) {
                    throw new Exception(Messages.getMessage("PDF_REMOVE_ERROR", entry.getAbsolutePath()));
                }

                Utils.printMessage(Messages.getMessage("REMOVE_TITLE"), Messages.getMessage("REMOVE_MESSAGE"));
                System.exit(0);

            }

            if (arguments(line, "entry", "remove", "database")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureFile(entry);

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);
                Publication publication = database.fromPDFtoPublication(entry);
                publication.sanitize();

                database.remove(publication);

                Utils.printMessage(Messages.getMessage("REMOVE_TITLE"), Messages.getMessage("REMOVE_MESSAGE"));
                System.exit(0);

            }

            if (arguments(line, "entry", "search", "tags")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                Database database = new Database(entry, false);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<Publication> result = database.searchTags(tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_TAGS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "entry", "search", "authors")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                Database database = new Database(entry, false);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthors(authors);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "entry", "search", "tags", "authors")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                Database database = new Database(entry, false);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthorsWithTags(authors, tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS_TAGS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "search", "tags")) {

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<Publication> result = database.searchTags(tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_TAGS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "search", "authors")) {

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthors(authors);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "search", "tags", "authors")) {

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthorsWithTags(authors, tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS_TAGS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "entry", "search", "tags")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);
                database.synchronizePublications(entry);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<Publication> result = database.searchTags(tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_TAGS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "entry", "search", "authors")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);
                database.synchronizePublications(entry);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthors(authors);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS"), Utils.buildEntries(result));
                System.exit(0);

            }

            if (arguments(line, "database", "entry", "search", "tags", "authors")) {

                File entry = new File(line.getOptionValue("entry"));
                Utils.ensureFileExists(entry);
                Utils.ensureDirectory(entry);

                File xml = new File(line.getOptionValue("database"));
                Utils.ensureFileExists(xml);
                Utils.ensureFile(xml);

                Database database = new Database(xml, true);
                database.synchronizePublications(entry);

                Set<String> tags = Utils.toSet(line.getOptionValue("tags"));
                Utils.ensureQuery(tags);

                Set<String> authors = Utils.toSet(line.getOptionValue("authors"));
                Utils.ensureQuery(authors);

                Set<Publication> result = database.searchAuthorsWithTags(authors, tags);

                Utils.printReport(Messages.getMessage("QUERY_RESULT_AUTHORS_TAGS"), Utils.buildEntries(result));
                System.exit(0);
            }

            throw new ParseException("quack");

        } catch (ParseException exception) {
            printHelp(options);
        } catch (Exception exception) {
            Utils.printException(exception, 0);
        }

    }

    /**
     * Exibe a ajuda do programa.
     * @param options Opções de linha de comando.
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Messages.getMessage("COMMAND_LINE"), options);
        System.exit(0);
    }

    /**
     * Verifica se a linha de comando apresenta os sinalizadores informados.
     * @param check Linha de comando.
     * @param values Sinalizadores a serem procurados.
     * @return Valor lógico indicando se todos os sinalizadores informados estão
     * presentes na linha de comando.
     */
    private static boolean arguments(CommandLine check, String... values) {
        for (String value : values) {
            if (!check.hasOption(value)) {
                return false;
            }
        }
        return check.getOptions().length == values.length;
    }

    /**
     * Valida o arranjo de valores opcionais, verificando se todos os valores
     * estão presentes.
     * @param optionals Arranjo de valores opcionais.
     * @return Valor lógico indicandos e todos os valores opcionais estão
     * presentes no arranjo informado.
     */
    private static boolean validate(Optional... optionals) {
        for (Optional opt : optionals) {
            if (!opt.isPresent()) {
                return false;
            }
        }
        return true;
    }

}
