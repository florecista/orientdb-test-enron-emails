package info.matthewryan.orientdb;

import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import info.matthewryan.orientdb.utils.Email;
import info.matthewryan.orientdb.utils.JSONUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.nio.file.Files.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class ApplicationTest {

    private final static String DB_DIR = "./target/db/enron";

    private static OrientGraphFactory factory;
    private OrientGraph oGraph;

    @BeforeClass
    public static void setupClass() throws IOException {
        Path root = Paths.get(DB_DIR);
        if (exists(root)) {
            walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {

                    delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {

                    delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        }
        factory = new OrientGraphFactory("plocal:" + DB_DIR);
    }

    @BeforeClass
    public static void teardownClass() {
        if (factory != null) {
            factory.close();
        }
    }

    @Before
    public void setup() throws IOException {
        oGraph = factory.getTx();
    }

    @After
    public void teardown() {
        if (oGraph != null) {
            oGraph.shutdown();
        }
    }

    private List<String[]> results = new ArrayList<>();

    private void loadTestData() {
        JSONUtils util = new JSONUtils();
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("FROM", "sender");
        propertyMap.put("TO", "recipients");
        propertyMap.put("CC", "cc");
        propertyMap.put("CONTENT_TYPE_KEY", "ctype");
        propertyMap.put("SUBJECT", "subject");
        propertyMap.put("IS_TREE", "false");
        List<Email> emails = util.parseEnronEmails("/enron.json", propertyMap);
        for (Email email : emails) {
            String[] record = new String[2];
            record[0] = email.getFrom();
            if(email.getTo().length > 0) {
                String[] toS = email.getTo();
                record[1] = toS[0];
                results.add(record);
            }
        }
    }
    private void loadVertices() {
        Map<String, Object> keyMap = new HashMap();

        for (String[] result : results) {
            OrientVertex orientVertex1 = oGraph.addVertex(result[0], "name", result[0]);
            OrientVertex orientVertex2 = oGraph.addVertex(result[1], "name", result[1]);
            orientVertex1.addEdge(result[0] + ":" + result[1], orientVertex2);
            orientVertex2.addEdge(result[1] + ":" + result[0], orientVertex1);
        }
        oGraph.commit();
    }

    @Test
    public void testVertices() {
        loadTestData();
        loadVertices();
        assertEquals(3220, Iterables.size(oGraph.getVertices()));
        assertTrue(oGraph.getVertices().iterator().hasNext());
    }
}
