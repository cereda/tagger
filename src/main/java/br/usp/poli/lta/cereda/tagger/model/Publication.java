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

import br.usp.poli.lta.cereda.tagger.utils.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Implementa uma publicação.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Publication {
    
    private long identifier;
    private String title;
    private List<String> authors;
    private Set<String> tags;
    private Set<File> references;
    
    /**
     * Construtor.
     */
    public Publication() {
    }
    
    /**
     * Construtor.
     * @param identifier Identificador.
     */
    public Publication(long identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Obtém o identificador.
     * @return Identificador.
     */
    public long getIdentifier() {
        return identifier;
    }
    
    /**
     * Define o identificador.
     * @param identifier Identificador.
     */
    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Obtém o título.
     * @return Título.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Define o título.
     * @param title Título.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Obtém a lista de autores.
     * @return Lista de autores.
     */
    public List<String> getAuthors() {
        return authors;
    }
    
    /**
     * Define a lista de autores.
     * @param authors Lista de autores.
     */
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
    
    /**
     * Obtém o conjunto de etiquetas.
     * @return Conjunto de etiquetas.
     */
    public Set<String> getTags() {
        return tags;
    }
    
    /**
     * Define o conjunto de etiquetas.
     * @param tags Conjunto de etiquetas.
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Define a operação de hash para objetos desta classe.
     * @return Valor inteiro indicando o hash do objeto corrente.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.identifier ^ (this.identifier >>> 32));
        return hash;
    }
    
    /**
     * Define a operação de igualdade entre publicações.
     * @param object Objeto a ser comparado com este.
     * @return Valor lógico indicando se os objetos são iguais.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Publication that = (Publication) object;
        return this.identifier == that.identifier;
    }
    
    /**
     * Obtém a lista de autores como um texto.
     * @return Texto referente à lista de autores.
     */
    public String getFlattenedAuthors() {
        return authors.stream().reduce((String t, String u) -> String.format("%s; %s", t, u)).orElse("<no authors given>");
    }
    
    /**
     * Obtém o conjunto de etiquetas como um texto.
     * @return Texto referente ao conjunto de etiquetas.
     */
    public String getFlattenedTags() {
        return tags.stream().map((UnaryOperator<String>) (String t) -> t.toLowerCase().trim()).reduce((String t, String u) -> String.format("%s; %s", t, u)).orElse("<no tags given>");
    }
    
    /**
     * Verifica se a publicação possui a etiqueta informada.
     * @param tag Etiqueta.
     * @return Valor lógico indicando se a publicação possui a etiqueta.
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
    
    /**
     * Remove a etiqueta do conjunto de etiquetas.
     * @param tag Etiqueta a ser removida.
     * @return Valor lógico indicando se a etiqueta foi removida com sucesso.
     */
    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }
    
    /**
     * Adiciona a etiqueta no conjunto de etiquetas.
     * @param tag Etiqueta a ser inserida.
     * @return Valor lógico indicando se a etiqueta foi inserida com sucesso.
     */
    public boolean addTag(String tag) {
        return tags.add(tag);
    }
    
    /**
     * Verifica se a publicação possui o autor informado.
     * @param author Autor.
     * @return Valor lógico indicando se a publicação possui o autor informado.
     */
    public boolean hasAuthor(String author) {
        return authors.contains(author);
    }
    
    /**
     * Adiciona o autor na lista de autores.
     * @param author Autor.
     * @return Valor lógico indicando se o autor foi inserido na lista de
     * autores.
     */
    public boolean addAuthor(String author) {
        return authors.add(author);
    }
    
    /**
     * Remove o autor da lista de autores.
     * @param author Autor.
     * @return Valor lógico indicando se o autor foi removido da lista de
     * autores.
     */
    public boolean removeAuthor(String author) {
        return authors.remove(author);
    }
    
    /**
     * Define o conjunto de etiquetas a partir de um texto.
     * @param text Texto.
     */
    public void setTagsFromString(String text) {
        setTags(Arrays.asList(text.split(";")).stream().map((UnaryOperator<String>) (String t) -> t.toLowerCase().trim()).collect(Collectors.toCollection(HashSet::new)));
    }
    
    /**
     * Define a lista de autores a partir de um texto.
     * @param text Texto.
     */
    public void setAuthorsFromString(String text) {
        setAuthors(Arrays.asList(text.split(";")).stream().map(String::trim).collect(Collectors.toList()));
    }
    
    /**
     * Verifica se a publicação possui uma das etiquetas informadas.
     * @param query Conjunto de etiquetas.
     * @return Valor lógico indicando se a publicação possui uma das etiquetas
     * informadas.
     */
    public boolean hasAnyTags(Set<String> query) {
        query = query.stream().map((UnaryOperator<String>) (String t) -> t.replaceAll("\\s+", " ").trim().toLowerCase()).collect(Collectors.toCollection(HashSet::new));
        return tags.stream().anyMatch(query::contains);
    }
    
    /**
     * Verifica se a publicação possui um dos autores informados.
     * @param query Conjunto de autores.
     * @return Valor lógico indicando se a publicação possui um dos autores
     * informados.
     */
    public boolean hasAnyAuthors(Set<String> query) {
        return authors.stream().anyMatch((String t) -> {
            return query.stream().anyMatch((element) -> (t.replaceAll("\\s+", " ").toLowerCase().trim().contains(element.toLowerCase().trim()) == true));
        });
    }
    
    /**
     * Realiza a limpeza dos campos de texto da publicação.
     */
    public void sanitize() {
        authors = authors.stream().map((UnaryOperator<String>) (String t) -> t.replaceAll("\\s+", " ").trim()).collect(Collectors.toList());
        tags = tags.stream().map((UnaryOperator<String>) (String t) -> t.replaceAll("\\s+", " ").trim().toLowerCase()).collect(Collectors.toCollection(HashSet::new));
        title = title.replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Realiza a limpeza das coleções da publicação.
     */
    public void cleanCollections() {
        authors = authors.stream().filter((String t) -> !t.trim().isEmpty()).collect(Collectors.toList());
        tags = tags.stream().filter((String t) -> !t.trim().isEmpty()).collect(Collectors.toCollection(HashSet::new));
    }
    
    /**
     * Define uma publicação em branco.
     * @return Uma publicação em branco.
     */
    public static Publication blank() {
        Publication p = new Publication();
        p.setAuthors(new ArrayList<>());
        p.setTags(new HashSet<>());
        p.setIdentifier(0);
        p.setTitle("");
        return p;
    }
    
    /**
     * Verifica se a publicação está em branco.
     * @return Valor lógico indicando se a publicação está em branco.
     */
    public boolean isBlank() {
        return authors.isEmpty() && tags.isEmpty() && title.isEmpty() && identifier == 0;
    }
    
    /**
     * Fornece uma descrição textual da publicação.
     * @return Descrição textual da publicação.
     */
    @Override
    public String toString() {
        
        String newline = "\n";
        
        if (isBlank()) {
            return Messages.getMessage("EMPTY_PUBLICATION");
        }
        
        sanitize();
        cleanCollections();
        
        StringBuilder main = new StringBuilder();
        
        main.append(WordUtils.wrap(Messages.getMessage("TITLE").toUpperCase().concat(": ").concat(title.isEmpty() ? Messages.getMessage("EMPTY_TITLE") : title), 70, newline, true));
        main.append(newline).append(StringUtils.repeat('.', 70)).append(newline);
        main.append(WordUtils.wrap(Messages.getMessage("AUTHORS").toUpperCase().concat(": ").concat(authors.isEmpty() ? Messages.getMessage("EMPTY_AUTHORS") : ""), 70, newline, true)).append(newline);
        if (!authors.isEmpty()) {
            authors.stream().forEach((author) -> {
                main.append(WordUtils.wrap("- ".concat(author), 70, newline, true)).append(newline);
            });
        }
        main.append(StringUtils.repeat('.', 70)).append(newline);
        main.append(WordUtils.wrap(Messages.getMessage("TAGS").toUpperCase().concat(": ").concat(tags.isEmpty() ? Messages.getMessage("EMPTY_TAGS") : ""), 70, newline, true));
        if (!tags.isEmpty()) {
            tags.stream().forEach((tag) -> {
                main.append(newline).append(WordUtils.wrap("- ".concat(tag), 70, newline, true));
            });
        }
        if (references != null && !references.isEmpty()) {
            main.append(newline).append(StringUtils.repeat('.', 70));
            main.append(newline).append(WordUtils.wrap(Messages.getMessage("FILE_REFERENCES").concat(":"), 70, newline, true));
            references.stream().forEach((file) -> {
                main.append(newline).append(WordUtils.wrap("- ".concat(file.getAbsolutePath()), 70, newline, true));
            });
        }
        return main.toString();
    }
    
    /**
     * Obtém as referências de arquivos da publicação.
     * @return Conjunto de arquivos.
     */
    public Set<File> getReferences() {
        return references;
    }
    
    /**
     * Define as referências de arquivos da publicação.
     * @param references Conjunto de arquivos.
     */
    public void setReferences(Set<File> references) {
        this.references = references;
    }
    
}
