# DEIWed

## Notas breves sobre a aplicação

A aplicação desenvolvida contém 4 views gerais sobre os objetos Attendees, Dishes, Orders e Sessions e 2 views dedicadas para todas as instâncias de Participantes e Sessões.

Desenvolvi a Home page, para que o acesso às diversas tabelas e funcionalidades seja mais intuitivo e a experiência de utilizador mais agradável.

Decidi que seria melhor ter uma view dedicada às Orders, para que o utilizador consiga gerir melhor os pedidos e ter maior controlo sobre estes. É nesta página que se emitem os pedidos (de forma manual).

A partir da tabela de Participantes, é possível eliminar uma entrada da tabela ou aceder a uma página dedicada que contém uma tabela de sessões a que o participante está inscrito. Aqui, é possível desinscrever o participante a uma dada sessão.

Os pratos estão disponíveis numa tabela de consulta rápida, onde é possível pesquisar e filtar pelos diversos parâmetros e distinguir facilmente os pratos normais dos vegetarianos.

Da lista de requisitos que me foi providenciada, faltou-me implementar a possibilidade das Sessões terem um poster. Tive alguma dificuldade em entender como é que se guardam imagens dentro de classes em Java e a forma como se armazenam na base de dados.

## Aspetos a melhorar e funcionalidades a implementar

Para além da lista de requisitos para este exercício, queria ainda indicar que funcionalidades acrescentaria, para melhorar a experiência do utilizador e a automatização dos processos, se tivesse mais tempo. Incorporava as possibilidades de:

- editar Participantes na página dedicada (ao contrário do atual);
- permitir inscrições em Sesões dentro da página dedicada de participantes;
- adicionar ao objeto Participante, como atributo, uma lista de sessões a que está inscrito;
- emitir um Pedido à empresa de _catering_ automaticamente ao criar uma Sessão;
- melhorar o design das páginas dedicadas;
- entre outros.
