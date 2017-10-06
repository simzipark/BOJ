import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/*
 * BOJ#14503 연구소 
 * https://www.acmicpc.net/problem/14502
 * 17-10-06
 */

public class Main {
	
	static final int BLANK = 0;
    static final int WALL = 1;
    static final int VIRUS = 2;
    static final int nADDWALL = 3;
    static final int[] dRow = {0, -1, 0, 1};	//바이러스 감염 경로 
    static final int[] dCol = {-1, 0, 1, 0};	//바이러스 감염 경로 

    static int N, M;
    static int[][] map = new int[9][9];
    static int nWall = 0;
    static int safetyMaxArea = -1;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		for(int i = 0; i < N; i++){
			
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < M; j++){
				
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == WALL) nWall++;
				
			}
		}
		
		//solve
		int[] wallPos = new int[nADDWALL];
		combination(wallPos, 0, 0, N*M, nADDWALL);
		
		System.out.println(safetyMaxArea);
		
	}
	
	static void combination(int[] arr, int depth, int target, int n, int k){
		/*
		 * int[] arr : wallPos that means ??? 3개짜리 int 배
		 * int target : 
		 * int n : size of map(N*M)
		 * int k : nADDWALL(3)
		 */
		
		//if depth is 3 which is maximum count of wall
		if(depth == k){
			commitMap(arr);		//벽까지 채워넣어 맵을 완성시키는 함수
			findSafetyArea();	//안전지역 갯수를 세는 함수 
			rollbackMap(arr);	//다시 맵을 원래상태로 돌려놓는 함
			return;
		}
		
		//if target become size of map(N*M)
		if(target == n){
			return;
		}
		
		//spot where can be wall
		if(map[target / M][target % M] == BLANK){	//주어진 맵의 좌표 하나씩 BLANK인지 확
			arr[depth] = target;	//BLANK면 target 숫자(0~n-1까지의 숫자)를 넣
			combination(arr, depth+1, target+1, n, k);
		}
		combination(arr, depth, target+1, n, k);
	
	}
	
	static void findSafetyArea(){
		
		boolean[][] visited = new boolean[N][M];
		for(int i = 0; i<N; i++){
			Arrays.fill(visited[i], false);	//배열을 모두 false로 채우기 
		} 
		
		int nVirusArea = 0;
		
		for(int i = 0; i<N; i++){
			for(int j = 0; j<M; j++){
				if(map[i][j] == VIRUS && !visited[i][j]){	//바이러스가 지나가는 if
					nVirusArea += dfs(i, j, visited);
				}
			}
		}
		
		safetyMaxArea = Math.max(safetyMaxArea, N*M - (nVirusArea + nWall + nADDWALL));
	}
	
	static int dfs(int row, int col, boolean[][] visited){
		
		visited[row][col] = true;	//감염된 곳을 true로
		
		int ret = 1;
		
		for(int i =0; i<4; i++){
			
			int nextRow = row + dRow[i];	//{0, -1, 0, 1};
			int nextCol = col + dCol[i];	//{-1, 0, 1, 0};
			
			if(nextRow < 0 || nextRow >=N || nextCol<0 || nextCol >= M) continue;	//감염 불가능 지역 
			
			if(map[nextRow][nextCol] != WALL && !visited[nextRow][nextCol])	//감염 가능 지역 
				ret += dfs(nextRow, nextCol, visited);
		}
		
		return ret;
	}
	
	static void commitMap(int[] arr){
		/*
		 * 벽이 세워진 위치를 기억하고있는 arr 변수로써 행과 열의 위치를 알아내
		 * 그 자리에 WALL, 즉 벽을 넣어
		 *	map을 완성시키는 함수 
		 */
		for(int i = 0; i<nADDWALL; i++){
			int row = arr[i] / M;
			int col = arr[i] % M;
			
			map[row][col] = WALL;
		}
	}
	
	static void rollbackMap(int[] arr){
		/*
		 * 벽이 세워진 위치를 기억하고있는 arr 변수로써 행과 열의 위치를 알아내
		 * 그 자리에 다시 BLANK를 넣어
		 * 원래의 map으로 돌려놓는 함수 
		 */
		for(int i = 0; i<nADDWALL; i++){
			int row = arr[i] / M;
			int col = arr[i] % M;
			
			map[row][col] = BLANK;
		}
	}

}
