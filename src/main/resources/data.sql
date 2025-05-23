
INSERT INTO Usuario (nome, email, senha, perfil)
VALUES
    ('João Silva', 'joao.silva@example.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC', 'ADMINISTRADOR'),
    ('Maria Souza', 'maria.souza@example.com', 'senha456', 'VOLUNTARIO'),
    ('Carlos Oliveira', 'carlos.oliveira@example.com', 'senha789', 'ADMINISTRADOR'),
    ('Ana Pereira', 'ana.pereira@example.com', 'senha321', 'CLIENTE');

INSERT INTO Produto (nome, preco, descricao, cor, tamanho, tipo, condicao, status, categoria)
VALUES
    ('Camiseta', 29.90, 'Camiseta de algodão', 'Azul', 'GG','CAMISETA', 'NOVO', 'DISPONIVEL', 'Roupa'),
    ('Calça Jeans', 79.90, 'Calça jeans confortável', 'Preto', 'M','CALCA', 'SEMI_NOVO', 'DISPONIVEL', 'Roupa'),
    ('Tênis Esportivo', 149.90, 'Tênis para corrida', 'Branco', '40','CALCADO', 'USADO', 'OCULTO', 'Roupa'),
    ('Jaqueta', 199.90, 'Jaqueta impermeável', 'Verde', 'P','BLUSA', 'NOVO', 'DISPONIVEL', 'Roupa'),
    ('Boné', 39.90, 'Boné ajustável', 'Cinza', 'G','OUTRO', 'NOVO', 'DISPONIVEL', 'Acessorio'),
    ('Mochila', 89.90, 'Mochila com várias divisórias', 'Preto', 'Grande','OUTRO', 'NOVO', 'DISPONIVEL', 'Roupa');
