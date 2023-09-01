-- Cria uma coluna que indica se o médico esta Ativo.
ALTER TABLE pacientes ADD ativo tinyint;

-- Como já existe dados cadastrados na tabela médicos, a nova coluna será setada como NULL nos campos já cadastrados.
-- Dessa maneira defino todos os médicos já cadastrados como ativo (1).
UPDATE pacientes SET ativo = 1;

-- Após todos os médicos estarem definidos como ativos, defino a coluna como NOT NULL, sendo assim agora será sempre
-- necessário informar se o médico que está sendo inserido já está ativo ou não.
ALTER TABLE pacientes MODIFY ativo TINYINT NOT NULL;