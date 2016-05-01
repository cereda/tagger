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
package br.usp.poli.lta.cereda.tagger.model;

import br.usp.poli.lta.cereda.tagger.utils.Utils;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Implementa métodos para manipulação de publicações.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Database {

    private final Map<Long, Publication> mapping;
    private final File location;
    private final boolean xml;

    /**
     * Construtor.
     * @param location Arquivo a ser analisado.
     * @param xml Sinalizador que indica se o arquivo é XML.
     */
    public Database(File location, boolean xml) {
        this.location = location;
        this.xml = xml;
        if (this.xml) {
            this.mapping = loadFromXML();
        } else {
            this.mapping = loadFromReference();
        }
    }

    /**
     * Carrega o mapa de publicações a partir de um arquivo XML.
     * @return Mapa contendo as publicações e seus respectivos identificadores.
     */
    private Map<Long, Publication> loadFromXML() {
        if (location.exists()) {
            try {
                XStream xstream = new XStream();
                xstream.alias("publication", Publication.class);
                xstream.omitField(Publication.class, "references");
                return (Map<Long, Publication>) xstream.fromXML(location);
            } catch (Exception exception) {
                return new HashMap<>();
            }
        } else {
            return new HashMap<>();
        }
    }

    /**
     * Sincroniza as publicações com os arquivos correspondentes em um
     * diretório informado.
     * @param directory Diretório contendo arquivos referentes às publicações.
     */
    public void synchronizePublications(File directory) {
        Map<Long, Set<File>> references = new HashMap<>();
        Collection<File> files = FileUtils.listFiles(directory, new String[]{"pdf", "PDF"}, true);
        long identifier;
        for (File file : files) {
            try {
                identifier = Utils.calculateChecksum(file);
                if (mapping.containsKey(identifier)) {
                    if (!references.containsKey(identifier)) {
                        references.put(identifier, new HashSet<>());
                    }
                    references.get(identifier).add(file);
                }
            } catch (Exception exception) {
                // quack
            }
        }
        references.keySet().stream().forEach((key) -> {
            mapping.get(key).setReferences(references.get(key));
        });
    }

    /**
     * Carrega o mapa de publicações a partir de um diretório contendo arquivos.
     * @return Mapa de publicações e seus respectivos identificadores.
     */
    private Map<Long, Publication> loadFromReference() {
        Map<Long, Publication> analysis = new HashMap<>();
        if (!location.isDirectory()) {
            try {
                analysis.put(Utils.calculateChecksum(location), extractMetadata(location));
            } catch (Exception exception) {
                // quack
            }
        } else {
            Collection<File> files = FileUtils.listFiles(location, new String[]{"pdf", "PDF"}, true);
            files.stream().forEach((file) -> {
                try {
                    long id = Utils.calculateChecksum(file);
                    if (!analysis.containsKey(id)) {
                        analysis.put(id, extractMetadata(file));
                    }
                } catch (Exception exception) {
                    // quack
                }
            });
        }
        return analysis;
    }

    /**
     * Realiza a gravação efetiva do mapa de publicações em um arquivo XML.
     */
    private void commit() {
        try {
            XStream xstream = new XStream();
            xstream.alias("publication", Publication.class);
            xstream.omitField(Publication.class, "references");
            xstream.toXML(mapping, new FileWriter(location));
        } catch (Exception exception) {
            // quack
        }
    }

    /**
     * Atualiza a publicação informada no mapa de publicações.
     * @param publication Publicação a ser atualizada no mapa de publicações.
     */
    public void update(Publication publication) {
        long id = publication.getIdentifier();
        if (mapping.containsKey(id)) {
            mapping.replace(id, publication);
        } else {
            mapping.put(id, publication);
        }
        if (xml) {
            commit();
        }
    }

    /**
     * Remove a publicação informada do mapa de publicações.
     * @param publication Publicação a ser removida do mapa de publicações.
     */
    public void remove(Publication publication) {
        long id = publication.getIdentifier();
        if (mapping.containsKey(id)) {
            mapping.remove(id);
        }
        if (xml) {
            commit();
        }
    }
 
    /**
     * Busca por etiquetas no mapa de publicações.
     * @param tags Conjunto de etiquetas.
     * @return Subconjunto do mapa contendo as etiquetas informadas.
     */
    public Set<Publication> searchTags(Set<String> tags) {
        return mapping.values().stream().filter((Publication t) -> t.hasAnyTags(tags)).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Busca por autores no mapa de publicações.
     * @param authors Conjunto de autores.
     * @return Subconjunto do mapa contendo os autores informados.
     */
    public Set<Publication> searchAuthors(Set<String> authors) {
        return mapping.values().stream().filter((Publication t) -> t.hasAnyAuthors(authors)).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Obtém uma possível publicação a partir do identificador informado.
     * @param identifier Identificador da publicação.
     * @return Uma possível publicação de acordo com o identificador informado.
     */
    public Optional<Publication> get(long identifier) {
        if (mapping.containsKey(identifier)) {
            return Optional.of(mapping.get(identifier));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Busca por autores e etiquetas no mapa de publicações.
     * @param authors Conjunto de autores.
     * @param tags Conjunto de etiquetas.
     * @return Subconjunto do mapa contendo os autores e etiquetas informados.
     */
    public Set<Publication> searchAuthorsWithTags(Set<String> authors, Set<String> tags) {
        List<Publication> people = mapping.values().stream().filter((Publication t) -> t.hasAnyAuthors(authors)).collect(Collectors.toList());
        return people.stream().filter((Publication t) -> t.hasAnyTags(tags)).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Converte os metadados do arquivo PDF informado para uma publicação, ou
     * simplesmente retorna a publicação já existente no mapa.
     * @param pdf Arquivo PDF.
     * @return Publicação referente ao arquivo informado.
     */
    public Publication fromPDFtoPublication(File pdf) {
        try {
            return mapping.getOrDefault(Utils.calculateChecksum(pdf), extractMetadata(pdf));
        } catch (Exception exception) {
            return Publication.blank();
        }
    }

    /**
     * Extrai os metadados do arquivo PDF em uma publicação.
     * @param file Arquivo PDF.
     * @return Publicação gerada a partir dos metadados do arquivo PDF.
     */
    private Publication extractMetadata(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            PdfReader reader = new PdfReader(input);
            HashMap<String, String> info = reader.getInfo();
            reader.close();
            Publication publication = new Publication(Utils.calculateChecksum(file));
            publication.setTitle(info.getOrDefault("Title", ""));
            publication.setAuthorsFromString(info.getOrDefault("Author", ""));
            publication.setTagsFromString(info.getOrDefault("Keywords", ""));
            publication.sanitize();
            return publication;
        } catch (Exception exception) {
            return Publication.blank();
        }
    }

    /**
     * Gera um novo arquivo PDF contendo os metadados informados.
     * @param from Arquivo PDF original.
     * @param to Novo arquivo PDF.
     * @param info Mapa de metadados.
     * @return Valor lógico indicando o sucesso ou falha da operação de geração.
     */
    private static boolean writePDF(File from, File to, HashMap<String, String> info) {
        try (FileInputStream input = new FileInputStream(from); FileOutputStream output = new FileOutputStream(to)) {
            PdfReader reader = new PdfReader(input);
            PdfStamper stamper = new PdfStamper(reader, output);
            stamper.setMoreInfo(info);
            stamper.close();
            reader.close();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Atualiza o arquivo PDF com os novos metadados.
     * @param file Arquivo PDF.
     * @return Valor lógico indicando o sucesso ou falha da operação de
     * atualização do arquivo PDF.
     */
    public boolean updatePDF(File file) {
        try {
            long identifier = Utils.calculateChecksum(file);
            if (mapping.containsKey(identifier)) {
                Publication publication = mapping.get(identifier);
                File update = new File((file.getAbsoluteFile().getParent() == null ? "" : file.getAbsoluteFile().getParent()).concat(File.separator).concat(FilenameUtils.getBaseName(file.getAbsolutePath()).concat(" (tagged).pdf")));
                HashMap<String, String> info = new HashMap<>();
                info.put("Author", publication.getFlattenedAuthors());
                info.put("Title", publication.getTitle());
                info.put("Keywords", publication.getFlattenedTags());
                info.put("Subject", "");
                info.put("Creator", "");
                info.put("Producer", "");
                System.out.println(info);
                return writePDF(file, update, info);
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Remove os metadados existentes do arquivo PDF informando, substituindo-os
     * por um mapa vazio.
     * @param file Arquivo PDF.
     * @return Valor lógico indicando sucesso ou falha da operação de remoção.
     */
    public static boolean removeDataFromPDF(File file) {
        File update = new File((file.getAbsoluteFile().getParent() == null ? "" : file.getAbsoluteFile().getParent()).concat(File.separator).concat(FilenameUtils.getBaseName(file.getAbsolutePath()).concat(" (untagged).pdf")));
        HashMap<String, String> info = new HashMap<>();
        info.put("Author", "");
        info.put("Title", "");
        info.put("Keywords", "");
        info.put("Subject", "");
        info.put("Creator", "");
        info.put("Producer", "");
        return writePDF(file, update, info);
    }

    /**
     * Obtém a publicação a partir de um mapa contendo apenas um elemento.
     * @return Publicação.
     */
    public Publication getSinglePublication() {
        return (mapping.size() != 1 ? Publication.blank() : mapping.get(mapping.keySet().iterator().next()));
    }

}
