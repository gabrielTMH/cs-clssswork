import memory.ManualMemoryManager;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static memory.MemoryManager.NONE;

public class ManualMemoryManagerTest {

	private ManualMemoryManager memory;
	
	@BeforeEach
	public void setUp() throws Exception {
		memory = new ManualMemoryManager(10);
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
		memory.allocate();
		memory.allocate();
		assertEquals(6, memory.unusedCount());
	}
	
	@Test
	public void testMemoryFull() {
		for (int i = 0; i < 5; i++) {
			memory.allocate();
		}
		assertEquals(0, memory.unusedCount());
		assertEquals(NONE, memory.allocate());
	}

	@Test
	public void testFree() {
		int address = memory.allocate();
		memory.allocate();
		memory.release(address);
		assertEquals(8, memory.unusedCount());
	}

}
