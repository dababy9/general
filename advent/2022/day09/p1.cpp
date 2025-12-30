#include <iostream>
#include <fstream>
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
	Point h = {0, 0}, t = {0, 0};
	char dir;
	int n;
	unordered_set<Point, PointHash> visited;
	while(fin >> dir >> n){
		for(int i = 0; i < n; i++){
			if(dir == 'L') h.x--;
			else if(dir == 'R') h.x++;
			else if(dir == 'D') h.y--;
			else h.y++;
			int dx = h.x-t.x, dy = h.y-t.y;
			if(abs(dx) > 1 || abs(dy) > 1){
				t.x += (dx == 0 ? 0 : (dx > 0 ? 1 : -1));
				t.y += (dy == 0 ? 0 : (dy > 0 ? 1 : -1));
			}
			visited.insert(t);
		}
	}
	cout << visited.size() << endl;
	return 0;
}