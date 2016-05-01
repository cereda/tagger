# tagger

`tagger` é um etiquetador de publicações.

```bash
[paulo@cambridge ~] $ java -jar tagger.jar 
  _                          
 | |_ __ _ __ _ __ _ ___ _ _ 
 |  _/ _` / _` / _` / -_) '_|
  \__\__,_\__, \__, \___|_|  
          |___/|___/         

tagger 1.0 -- Etiquetador de publicações
Laboratório de Linguagens e Técnicas Adaptativas
Escola Politécnica, Universidade de São Paulo

usage: tagger [ --entry <file> | --database <file> ] [ --remove | --update
              | --search [ --tags | --authors ] ]
 -a,--authors <arg>   filtro de autores
 -d,--display         exibe metadados da publicação
 -e,--entry <arg>     arquivo/diretório a ser analisado
 -h,--help            exibe a ajuda
 -r,--remove          remove metadados da publicação
 -s,--search          busca de publicações
 -t,--tags <arg>      filtro de etiquetas
 -u,--update          atualiza metadados da publicação
```
