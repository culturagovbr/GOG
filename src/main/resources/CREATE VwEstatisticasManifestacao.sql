CREATE VIEW "VwEstatisticasManifestacao"
AS
/*--------Estatísticas comuns*/ SELECT 1 AS codMetrica, 'Abertas hoje' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '1' and cast(dtCadastro As Date) = cast(CURRENT_DATE As Date)
UNION ALL
SELECT        2 AS codMetrica, 'Fechadas hoje' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao IN ('4', '5') AND cast(dtCadastro As Date) = cast(CURRENT_DATE As Date)
UNION ALL
SELECT        3 AS codMetrica, 'Não encaminhadas' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '1'
UNION ALL
SELECT        4 AS codMetrica, 'Total abertas' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao NOT IN ('4', '5')
UNION ALL
SELECT        5 AS codMetrica, 'Total de Manifestações' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
/*--------Estatísticas por status da manifestação*/ UNION ALL
SELECT        6 AS codMetrica, 'Nova' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '1'
UNION ALL
SELECT        7 AS codMetrica, 'Em andamento' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '2'
UNION ALL
SELECT        9 AS codMetrica, 'Solucionada' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '4'
UNION ALL
SELECT        8 AS codMetrica, 'Encerrada' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m
WHERE        stStatusManifestacao = '5'
/*--------Estatísticas por status do Tramite*/ UNION ALL
SELECT        10 AS codMetrica, 'Encaminhada' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m INNER JOIN
                         tbEncaminhamento enc ON (enc.idManifestacao = m.idManifestacao)
WHERE        m.stStatusManifestacao IN ('2', '3') AND enc.stEncaminhamento = '1'
UNION ALL
SELECT        11 AS codMetrica, 'Retornada' AS dsMetrica, COUNT(m.idManifestacao) AS qtdade
FROM            tbManifestacao m INNER JOIN
                         tbEncaminhamento enc ON (enc.idManifestacao = m.idManifestacao)
WHERE        m.stStatusManifestacao IN ('2', '3') AND enc.stEncaminhamento = '2'

