import memory.MemoryManager;
import memory.ReferenceCounter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static memory.MemoryManager.NONE;

public class ReferenceCounterTest {

	private MemoryManager memory;
	
	@BeforeEach
	public void setUp() throws Exception {
		memory = new ReferenceCounter(10);
	}

	@Test
	public void testUnusedCount() {
		assertEquals(10, memory.unusedCount());
	}

	@Test
	public void testAllocate() {
		int address = memory.allocate();
		assertEquals(8, memory.unusedCount());
		assertEquals(NONE, memory.get(address));
		assertEquals(NONE, memory.get(address + 1));
	}
	
	@Test
	public void testSetRoot() {
		memory.setRoot(memory.allocate());
		memory.setRoot(memory.allocate());
		assertEquals(8, memory.unusedCount());
	}
	
	@Test
	public void testMemoryFull() {
		for (int i = 0; i < 5; i++) {
			memory.allocate();
		}
		assertEquals(0, memory.unusedCount());
	}

	@Test
	public void testLinkedList() {
		memory.setRoot(memory.allocate());
		for (int i = 0; i < 4; i++) {
			int address = memory.allocate();
			memory.set(address, memory.getRoot());
			memory.setRoot(address);
		}
		assertEquals(0, memory.unusedCount());
		assertEquals(NONE, memory.allocate());
		memory.setRoot(NONE);
		assertEquals(10, memory.unusedCount());
	}

	@Test
	public void testTree() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		memory.set(a, b);
		memory.set(a + 1, c);
		memory.set(c, d);
		memory.setRoot(a);
		assertEquals(2, memory.unusedCount());
		memory.setRoot(NONE);
		assertEquals(10, memory.unusedCount());		
	}

	@Test
	public void testDirectedAcyclicGraph() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		memory.set(a, b);
		memory.set(a + 1, c);
		memory.set(b, d);
		memory.set(c, d);
		memory.setRoot(a);
		assertEquals(2, memory.unusedCount());
		memory.setRoot(NONE);
		assertEquals(10, memory.unusedCount());				
	}
	
	@Test
	public void testCyclicGraph() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		memory.set(a, b);
		memory.set(b, c);
		memory.set(c, d);
		memory.set(d + 1, b);
		memory.setRoot(a);
		assertEquals(2, memory.unusedCount());
		memory.setRoot(NONE);
		// Reference counting can't reclaim the cycle
		assertEquals(4, memory.unusedCount());
	}

	@Test
	public void testSet() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		memory.set(a, b);
		memory.set(a + 1, c);
		memory.set(c, d);
		memory.setRoot(a);
		// Make c and d unreachable
		memory.set(a + 1, NONE);		
		assertEquals(6, memory.unusedCount());
	}

	@Test
	public void testSelfLoop() {
		memory.setRoot(memory.allocate());
		memory.set(memory.getRoot(), memory.getRoot());
		assertEquals(8, memory.unusedCount());
	}
	
	@Test
	public void testSavePart() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		memory.set(a, b);
		memory.set(a + 1, c);
		memory.set(b, d);
		memory.set(c, d);
		memory.setRoot(a);
		assertEquals(2, memory.unusedCount());
		// Make a and c unreachable
		memory.setRoot(b);
		assertEquals(6, memory.unusedCount());			
	}


}
