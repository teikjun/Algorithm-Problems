#include <algorithm>
#include <iostream>
#include <vector>
#include <stdio.h>
using namespace std;

bool isRouterDistancePossible(int routerDistance, const vector<int> & house, int availableRouters) {
    int lastRouter = house[0] + routerDistance;
    int routersUsed = 1;
    for (int i = 1; i < house.size() && routersUsed <= availableRouters; i++) {
        if (house[i] - lastRouter > routerDistance) {
            lastRouter = house[i] + routerDistance;
            routersUsed++;
        }
    }
    return routersUsed <= availableRouters;
}

int main() {

    int cases;
    for (scanf("%d", &cases); cases; cases--) {

        int availableRouters, houseCount;
        cin >> availableRouters >> houseCount;
        if (houseCount == 0) {
            cout << 0 << endl;
            continue;
        }
        vector<int> house;
        for (int i = 0; i < houseCount; i++) {
            int houseNumber;
            scanf("%d", &houseNumber);
            houseNumber *= 10;
            house.push_back(houseNumber);
        }
        sort(house.begin(), house.end());

        int sol = -1;
        int start = 0, end = house[houseCount - 1] - house[0];
        while (start <= end) {
            int mid = (start + end) / 2;
            if (isRouterDistancePossible(mid, house, availableRouters)) {
                sol = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        cout << sol / 10 << "." << sol % 10 << endl;

    }

    return 0;
}
