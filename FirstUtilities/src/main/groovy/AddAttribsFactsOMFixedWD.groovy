import liqp.parser.LiquidSupport
import liqp.Template

guidsToAddFile = getClass().getResourceAsStream("/GuidsAttrAndFacts.txt")
pointers338File = getClass().getResourceAsStream("/338Pointers.txt")

template = Template.parse("""
declare @root uniqueidentifier = (  select  guidobject from babizagicatalog where objtype = 121 );
declare @guidWD uniqueidentifier = '3BCD0861-536E-49FB-B530-8FE0721F362D' 
declare @modifiedByUser varchar(50) = 'OldmutualEntitySchemaDependencies'; 
declare @changeSetId int
BEGIN
set @changeSetId = (  select  max(changesetid)+ 1 from babizagicatalog  );
update BABIZAGICATALOG set objcontent = dbo.fnBA_DB_ClobToBlob(replace(dbo.fnBA_DB_BlobToClob(objcontent), '}]}', '},<V>]}')),
objContentResolved = dbo.fnBA_DB_ClobToBlob(replace(dbo.fnBA_DB_BlobToClob(objContentResolved), '}]}', '},<V>]}'))
where objTypeName = 'WorkflowDependencies'
and guidObject = @guidWD;
{% for baref in barefs %}
Insert into bacatalogreference values (@root, '{{baref.Pointer}}', @guidWD, '{{baref.Target | downcase}}', 0);{% endfor %}

Update BABIZAGICATALOG	set 
objcontent = dbo.fnBA_DB_ClobToBlob(replace(dbo.fnBA_DB_BlobToClob(objcontent), '<V>', '{% for baref in barefs %}{"baref":{"ref":"{{baref.Pointer}}"}}{% unless forloop.last %},{% endunless -%}{% endfor %}')),
objContentResolved = dbo.fnBA_DB_ClobToBlob(replace(dbo.fnBA_DB_BlobToClob(objContentResolved), '<V>', '{% for baref in barefs %}{"baref":{"ref":"{{baref.Target | downcase}}"}}{% unless forloop.last %},{% endunless -%}{% endfor %}')),
changeSetId = @changeSetId, modifiedByUser = @modifiedByUser where guidObject = @guidWD;
END;
""")

class Baref implements LiquidSupport{
    String pointer
    String target

    Baref(String pointer, String target){
        this.pointer = pointer
        this.target = target
    }

    @Override
    Map<String, Object> toLiquid() {
        return [Pointer: pointer, Target: target] as Map<String, Object>
    }
}

refs = []
pointerLines = pointers338File.readLines()
i = 0
guidsToAddFile.eachLine { line ->
    refs.add(new Baref(pointerLines[i], line))
    i++
}

def binding = [barefs: refs]

render = template.render(binding)

outFile = new File("C:\\Users\\maxne\\Downloads\\OldMutualAddAttribsAndFactsInWD_v0.sql")
writer = new FileWriter(outFile)
writer.write(render)
writer.close()