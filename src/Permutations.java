import java.util.Arrays;

public class Permutations {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Permutations main = new Permutations();
		String[] s = { "d", "b", "c", "a" };
		main.Permutation(s);
		System.out.println("*************************");
		main.Permutation(new String[] { "a", "b", "c", "d" });
	}

	public void Permutation(String[] s) {
		int[] a = new int[s.length];
		backtrack(a, 0, s);
	}

	/*
	 * The first k-1 indices is stored in a, here we need to choose the kth
	 * element;
	 */
	public void backtrack(int[] a, int k, String[] s) {
		// if we reach the end;
		if (is_a_solution(a, k, s)) {
			// print(a, k);
			process_solution(a, k, s);
			return;
		}

		int[] candidates = construct_candidates(a, k, s);
		// k = k + 1;
		for (int i = 0; i < candidates.length; i++) {
			a[k] = candidates[i];
			// make_move(a,k,s);
			int[] tmp = Arrays.copyOf(a, s.length);
			backtrack(a, k + 1, s);
			// unmake_move(a,k,s);
			a = Arrays.copyOf(tmp, s.length);

			// check if finished;
		}
	}

	/*
	 * Get all the indices of elements which are not in the permutation list;
	 */
	public int[] construct_candidates(int[] a, int k, String[] s) {
		int n = s.length;
		boolean[] in_perm = new boolean[n];
		// Mark all the indices of elements in the permutation as true;
		for (int i = 0; i < k; i++)
			in_perm[a[i]] = true;
		int[] ret = new int[n - k];
		int count = 0;
		// Count the unlisted elements' indices;
		for (int i = 0; i < n; i++) {
			if (in_perm[i] == false) {
				ret[count++] = i;
			}
		}
		return ret;
	}

	/*
	 * Process the permutation, which is the array of the indices;
	 */
	public void process_solution(int[] a, int k, String[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[a[i]]);
			System.out.print(" ");
		}
		System.out.println();
	}

	public boolean is_a_solution(int[] a, int k, String[] s) {
		// Reach the limit, which there is no element here;
		int n = s.length;
		return k == n;
	}

	// Check the permutation indices;
	public void print(int[] a, int k) {
		for (int i = 0; i < k; i++, System.out.print(" "))
			System.out.print(a[i]);
		System.out.println();
	}
}
