# Backend

Equipe Alpha: Ayanne Prata, Danilo Costa, Keilla Vitória

---
### - Como usar:

O backend está configurado para utilizar o postgreSQL como banco de dados, de modo que o mesmo deve ser instalado na sua máquina, segue [link](http:www.postgresql.org/download/windows)

Quando instalar o postgreSQL sua senha deve ser informada em `application.properties` nesse projeto, no caminho **src/main/recourse**

O banco não cria automaticamente a _database_ a ser usada. Isto deve ser feito por meio do postgreSQL. Entre no programa e crie uma _database_ com título "SchedulingSportPractices" **ou** crie um com outro nome, mas modifique o informado em `application.properties`.

Para executar a aplicação basta rodar a classe em /SchedulingSportsPractices/src/main/java/br/edu/ifpb/dac/ssp

---