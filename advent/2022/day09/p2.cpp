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

void compute(Point& h, Point& t){
	int dx = h.x-t.x, dy = h.y-t.y;
	if(abs(dx) > 1 || abs(dy) > 1){
		t.x += (dx == 0 ? 0 : (dx > 0 ? 1 : -1));
		t.y += (dy == 0 ? 0 : (dy > 0 ? 1 : -1));
	}
}

int main(){
	ifstream fin("input.txt");
	Point rope[10] = {};
	char dir;
	int n;
	unordered_set<Point, PointHash> visited;
	while(fin >> dir >> n){
		for(int i = 0; i < n; i++){
			if(dir == 'L') rope[0].x--;
			else if(dir == 'R') rope[0].x++;
			else if(dir == 'D') rope[0].y--;
			else rope[0].y++;
			for(int j = 1; j < 10; j++)
				compute(rope[j-1], rope[j]);
			visited.insert(rope[9]);
		}
	}
	cout << visited.size() << endl;
	return 0;
}