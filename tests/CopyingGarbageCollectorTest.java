import memory.CopyingGarbageCollector;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static memory.MemoryManager.NONE;

public class CopyingGarbageCollectorTest {

	private CopyingGarbageCollector memory;
	
	@BeforeEach
	public void setUp() throws Exception {
		memory = new CopyingGarbageCollector(10);
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
		assertEquals(6, memory.unusedCount());
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
		// Force garbage collection
		memory.allocate();
		assertEquals(8, memory.unusedCount());
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(8, memory.unusedCount());		
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(8, memory.unusedCount());
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(8, memory.unusedCount());
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(4, memory.unusedCount());
	}

	@Test
	public void testSelfLoop() {
		memory.setRoot(memory.allocate());
		memory.set(memory.getRoot(), memory.getRoot());
		// Force garbage collection
		for (int i = 0; i < 5; i++) {
			memory.allocate();
		}
		assertEquals(6, memory.unusedCount());
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(4, memory.unusedCount());		
	}
	
	@Test
	public void testMovement() {
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
		// Force garbage collection
		memory.allocate();
		memory.allocate();
		assertEquals(0, memory.unusedCount());
		// Structure should have been moved into other side of memory
		assertEquals(10, memory.getRoot());
		// Both paths should lead to the same node
		assertEquals(memory.get(memory.get(memory.getRoot())), memory.get(memory.get(memory.getRoot() + 1)));
		// Force another garbage collection
		memory.allocate();
		// Structure should have been moved into other side of memory
		assertEquals(0, memory.getRoot());
		// Both paths should lead to the same node
		assertEquals(memory.get(memory.get(memory.getRoot())), memory.get(memory.get(memory.getRoot() + 1)));
	}

	@Test
	public void testCheckBothCells() {
		int a = memory.allocate();
		int b = memory.allocate();
		int c = memory.allocate();
		int d = memory.allocate();
		int e = memory.allocate();
		memory.set(a, b);
		memory.set(a + 1, c);
		memory.set(b, d);
		memory.set(c, e);
		memory.setRoot(a);
		assertEquals(0, memory.unusedCount());
		// Force garbage collection
		assertEquals(NONE, memory.allocate());
		assertEquals(0, memory.unusedCount());
	}

	@Test
	public void testDoesNothingForNullRoot() {
		for (int i = 0; i < 6; i++) {
			memory.allocate();
		}
		assertEquals(NONE, memory.getRoot());
	}

}
