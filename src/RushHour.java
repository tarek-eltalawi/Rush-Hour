import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;


public class RushHour {
	
	public Scanner in;
	
	public PriorityQueue<State> pq;
	
	private LinkedList<State> searchAStar(Puzzle puzzle, Heuristic heuristic) {
		HashMap<State, State> predecessor = new HashMap<>();
		HashMap<String, Boolean> visited = new HashMap<>();
		State src = new State(puzzle);
		State goal = null;
		src.setCost(0);
		pq.add(src);
		visited.put(src.toString(), true);
		while(!pq.isEmpty()){
			State u = pq.poll();
			if(u.isGoal()){
				goal = u;
				break;
			}
			for(State v : u.getNeighbors()){
				int cost = u.cost + 1 + heuristic.getValue(v);
				if(!contains(visited, v)){
					v.setCost(cost);
					pq.add(v);
					predecessor.put(v, u);
					visited.put(v.toString(), true);
				}
			}
		}
		
		return getPath(predecessor, goal);
	}

	private boolean contains(HashMap<String, Boolean> visited, State v) {
		return visited.containsKey(v.toString());
	}

	private void print(LinkedList<State> path) {
		System.out.println("Number of optimal movements = " + (path.size()-1) + "\n");
		int index = 0;
		for(State s : path){
			if(index == 0)
				System.out.println("Initial state:");
			else
				System.out.println("Step " + index + ":");
			s.print();
			index ++;
		}
	}

	private LinkedList<State> getPath(HashMap<State, State> pred, State goal) {
		
		LinkedList<State> path =  new LinkedList<>();
		State u = goal;
		path.addFirst(u.clone());
		while(pred.get(u) != null){
			State parent = pred.get(u);
			path.addFirst(parent.clone());
			u = parent;
		}

		return path;
	}

	private Puzzle readInput() {
		
		LinkedList<Car> cars = new LinkedList<>();
		
		String line = in.nextLine();
		int size = line.length();
		char[][] grid = new char[size][size];
		grid[0] = line.toCharArray();
		for (int i = 1; i < grid.length; i++) {
			line = in.nextLine();
			grid[i] = line.toCharArray();
		}

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				switch (grid[i][j]) {
					case '+': grid[i+1][j] = '.'; cars.add(new Car(i, j, "v", 2)); break;
					case '*': grid[i][j+1] = '.'; cars.add(new Car(i, j, "h", 2)); break;
					case '@': grid[i+1][j] = '.'; grid[i+2][j] = '.'; cars.add(new Car(i, j, "v", 3)); break;
					case '#': grid[i][j+1] = '.'; grid[i][j+2] = '.'; cars.add(new Car(i, j, "h", 3)); break;
					// red car represented by '=' and added at top of list
					case '=': grid[i][j+1] = '.'; cars.addFirst(new Car(i, j, "h", 2)); break;
				}
			}
		}
		
		return new Puzzle(size, cars);
	}
	
	private void run() {
		pq = new PriorityQueue<State>(10, new Comparator<State>() {
			
			@Override
			public int compare(State o1, State o2) {
				return o1.cost - o2.cost;
			}
			
		});
		
		in = new Scanner(System.in);
		
		Puzzle puzzle = readInput();
		
		Heuristic heuristic1 = new Heuristic1();
		Heuristic heuristic2 = new Heuristic2();
		
		long startTime = System.currentTimeMillis();
		LinkedList<State> path1 = searchAStar(puzzle, heuristic1);
		long endTime = System.currentTimeMillis();
		long timeTaken1 = endTime - startTime;
		
		startTime = System.currentTimeMillis();
		LinkedList<State> path2 = searchAStar(puzzle, heuristic2);
		endTime = System.currentTimeMillis();
		long timeTaken2 = endTime - startTime;
		
		System.out.println("Solution using Heuristic.");
		System.out.println("#########################");
		print(path1);
		System.out.println("Time taken using heuristic : " + timeTaken1);
		System.out.println();
		
		System.out.println("Solution with no Heuristic used.");
		System.out.println("################################");
		print(path2);
		System.out.println("Time taken without using heuristic : " + timeTaken2);
		
		in.close();
	}
	
	public static void main(String[] args) {
		
		RushHour THIS = new RushHour();
		THIS.run();
	}
}
