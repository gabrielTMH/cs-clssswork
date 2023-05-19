package memory;

/**
 * A memory manager. This class is declared abstract because it provides no way
 * to reclaim memory that is no longer in use. Subclasses should either provide
 * a release() method or automatically reclaim memory by overriding methods
 * provided here.
 */
public abstract class MemoryManager {

	/** Memory cells. */
	private int[] memory;

	/** Address of a "null" pointer. */
	public static final int NONE = -1;

	/** Address of the root pointing into memory. */
	private int root;

	/** Address of first unused pair of memory cells. */
	private int free;

	public MemoryManager(int capacity) {
		root = NONE;
		memory = new int[capacity];
		free = NONE;
		for (int i = 0; i < capacity; i += 2) {
			memory[i] = free;
			free = i;
		}
	}

	/**
	 * Returns the address of the first of a pair of unused memory locations,
	 * removing them from the pool of unused memory. These two cells are given
	 * the value NONE. If necessary, performs garbage collection to free up some
	 * memory. Returns NONE if there is no memory unused.
	 */
	public int allocate() {
		if (free == NONE) {
			return NONE;
		}
		int result = free;
		free = memory[result];
		memory[result] = NONE;
		memory[result + 1] = NONE;
		return result;
	}

	/**
	 * Gets the value stored at address. It is an error to get a value in unused
	 * memory.
	 */
	public int get(int address) {
		return memory[address];
	}

	/** Returns the total number of cells in memory. */
	public int getCapacity() {
		return memory.length;
	}

	/** Returns the address of the first unused pair of memory cells. */
	protected int getFree() {
		return free;
	}

	/** Returns the root pointer. */
	public int getRoot() {
		return root;
	}

	/**
	 * Sets the value stored at address. It is an error to set a value in unused
	 * memory, or to store a pointer into unused memory.
	 */
	public void set(int address, int value) {
		memory[address] = value;
	}

	/** Stores free as the address of the first unused pair of memory cells. */
	protected void setFree(int free) {
		this.free = free;
	}

	/**
	 * Sets the root pointer to address. The address to which the root
	 * previously pointed (if any) is no longer pointed to directly by the root,
	 * but may be reachable indirectly. It is an error to set the root to point
	 * into unused memory. SUBCLASSES WILL WANT TO OVERRIDE THIS.
	 */
	public void setRoot(int address) {
		root = address;
	}

	/** Returns the number of unused memory locations. */
	public int unusedCount() {
		int result = 0;
		for (int i = free; i != NONE; i = get(i)) {
			result += 2;
		}
		return result;
	}

}
