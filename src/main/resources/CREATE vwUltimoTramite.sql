CREATE VIEW "vwUltimoTramite"
 as 
 select ultTram.*,
 t.idUsuarioReceptor
from tbTramite t
inner join (
   select m.idManifestacao,e.idEncaminhamento,max(t.idTramite) as idTramite
   from tbManifestacao m
   inner join tbEncaminhamento e on e.idManifestacao = m.idManifestacao
   inner join tbTramite t on t.idEncaminhamento = e.idEncaminhamento
   group by m.idManifestacao,e.idEncaminhamento
) ultTram on ultTram.idTramite = t.idTramite;
