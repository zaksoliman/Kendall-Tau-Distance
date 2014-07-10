
def all_marked(arr):

    allMarked = True

    for x in arr:
        if not x:
            allMarked = False
            break

    return allMarked

permutation = [4, 1, 2, 3, 5]
cycles = set()

marked = [False for x in permutation]

current = 1
cycle = [current]

while not all_marked(marked):

    nextElem = permutation[current-1]

    if nextElem == current:
        cycles.add(tuple(cycle))
        del cycle[:]
        marked[current-1] = True
    else:
        current = nextElem
        cycle.append(current)
        marked[current-1] = True
