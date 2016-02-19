create schema DBD; 
use DBD;   

create table Cliente(
	cpf varchar(11) primary key,
    nome_cliente varchar(30),
    endereco varchar (80),      
    telefone varchar(8),
    pontos_fedelidade int   
) engine=innodb;

create table Pedido(
    pedido_Id int auto_increment primary key,
    valor_total decimal(4,2),      
    forma_pagamento varchar(10),
    entregador varchar(30),
    status varchar(10),
    cpf varchar(11),
    foreign key (cpf) references Cliente(cpf)
) engine=innodb;

create table Item_Cardapio(
    nome_item varchar (30) primary key,
    preco decimal(4,2), 
    popularidade int,
    pontos_fidelidade_item int,     
    ingredientes varchar(30),
    disponibilidade bit,
    a_venda bit 
) engine=innodb;

create table Entrada(
    nome_item varchar (30),
    foreign key (nome_item) references Item_Cardapio(nome_item)
) engine=innodb;

create table Principal(
    nome_item varchar (30),
    foreign key (nome_item) references Item_Cardapio(nome_item)
) engine=innodb;

create table Bebida(
    nome_item varchar (30),
    foreign key (nome_item) references Item_Cardapio(nome_item)
) engine=innodb;

create table Sobremesa(
    nome_item varchar (30),
    foreign key (nome_item) references Item_Cardapio(nome_item)
) engine=innodb;

create table Lista_Item(
    nome_item varchar (30),
    pedido_Id int,
    quantidade int,
    foreign key (pedido_Id) references Pedido(pedido_Id),
    foreign key (nome_item) references Item_Cardapio(nome_item)
) engine=innodb;

create table Empresa(
    empresa_Id int auto_increment primary key,
    caixa decimal(5,2),      
    nome_usuario varchar(10),
    senha_usuario varchar(10)
) engine=innodb;