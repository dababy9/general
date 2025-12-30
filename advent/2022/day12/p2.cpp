#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <cmath>
using namespace std;

struct Point {
	int x, y;
	bool operator==(const Point& other) const {
		return x == other.x && y == other.y;
	}
};

struct PointHash {
	size_t operator()(const Point& p) const {
		return hash<int>()(p.x) ^ (hash<int>()(p.y) << 1);
	}
};

int main(){
	ifstream fin("input.txt");
	string s;
	vector<string> grid;
	Point start;
	for(int x = 0; getline(fin, s); x++){
		size_t index = s.find('S');
		if(index != string::npos) s[index] = 'a';
		index = s.find('E');
		if(index != string::npos){
			s[index] = 'z';
			start = {x, static_cast<int>(index)};
		}
		grid.push_back(s);
	}
	queue<Point> q;
	q.push(start);
	unordered_set<Point, PointHash> vis;
	vis.insert(start);
	int delta[4][2] = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	for(int steps = 1; true; steps++){
		queue<Point> adj;
		while(!q.empty()){
			Point p = q.front();
			q.pop();
			for(int i = 0; i < 4; i++){
				Point q = {p.x + delta[i][0], p.y + delta[i][1]};
				if(q.x < 0 || q.y < 0 || q.x >= grid.size() || q.y >= s.size()) continue;
				if(grid[p.x][p.y] - grid[q.x][q.y] <= 1 && vis.find(q) == vis.end()){
					if(grid[q.x][q.y] == 'a'){
						cout << steps << endl;
						return 0;
					}
					vis.insert(q);
					adj.push(q);
				}
			}
		}
		q = adj;
	}
	return 0;
}