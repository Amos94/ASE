package context;

import java.io.File;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

public class ContextHelper {

    private String ctxsDir;

    public ContextHelper(String ctxsDir) {
        this.ctxsDir = ctxsDir;
    }

    public void run() {

        System.out.printf("looking (recursively) for solution zips in folder %s\n",
                new File(ctxsDir).getAbsolutePath());

        Set<String> slnZips = IoHelper.findAllZips(ctxsDir);

        for (String slnZip : slnZips) {
            System.out.println("%n#### processing the events#####%n");
            processZip(slnZip);
        }
    }

    public void processZip(String slnZip) {
        try (IReadingArchive ra = new ReadingArchive(new File(ctxsDir, slnZip))) {
            while (ra.hasNext()) {
                Context ctx = ra.getNext(Context.class);

                processContext(ctx);
                process(ctx.getSST());
            }
        }
    }

    public ISST getSST(Context ctx){
        return ctx.getSST();
    }

    public ITypeShape getTypeShape(Context ctx){
        return ctx.getTypeShape();
    }

    public void processContext(Context ctx) {
        process(ctx.getSST());
        process(ctx.getTypeShape());
    }

    public void process(ISST sst) {
        ITypeName declType = sst.getEnclosingType();

        for (IMethodDeclaration md : sst.getMethods()) {
            IMethodName m = md.getName();

            for (IStatement stmt : md.getBody()) {
                stmt.accept(new ExampleVisitor(), null);
            }

        }

        /*
           all references to types or type elements are fully qualified and preserve
           many information about the resolved type
         */
        declType.getNamespace();
        declType.isInterfaceType();
        declType.getAssembly();

        // you can distinguish reused types from types defined in a local project
        boolean isLocal = declType.getAssembly().isLocalProject();

        // the same is possible for all other <see>IName</see> subclasses, e.g.,
        // <see>IMethodName</see>
        IMethodName m = Names.getUnknownMethod();
        m.getDeclaringType();
        m.getReturnType();
        // or inspect the signature
        for (IParameterName p : m.getParameters()) {
            String pid = p.getName();
            ITypeName ptype = p.getValueType();
        }
    }

    public void process(ITypeShape ts) {
        // a type shape contains hierarchy info for the declared type
        ITypeHierarchy th = ts.getTypeHierarchy();
        // the type that is being declared in the SST
        ITypeName tElem = th.getElement();
        // the type might extend another one (that again has a hierarchy)
        if (th.getExtends() != null) {
            ITypeName tExt = th.getExtends().getElement();
        }
        // or implement interfaces...
        for (ITypeHierarchy tImpl : th.getImplements()) {
            ITypeName tInterf = tImpl.getElement();
        }

        Set<IMemberHierarchy<IMethodName>> mhs = ts.getMethodHierarchies();
        for (IMemberHierarchy<IMethodName> mh : mhs) {
            IMethodName elem = mh.getElement();
            IMethodName sup = mh.getSuper();
            IMethodName first = mh.getFirst();
        }

        ts.getDelegates();
        ts.getEventHierarchies();
        ts.getFields();
        ts.getPropertyHierarchies();
        ts.getNestedTypes();
    }

    public class ExampleVisitor extends AbstractTraversingNodeVisitor<Object, Object> {
    }

}
