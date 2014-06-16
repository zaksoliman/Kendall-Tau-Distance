
#Builds the medians and writes results to a file
def buildMedian(value, permSet):
    minDist = float('inf') #holds the distance of the closest permutation in permSet
    closestPermutations = []
    print('Value='+str(value))
    #Get the closest permutations
    for permutation in permSet:
        distance = KendallTauDistance(permutation, permSet)
        if distance < minDist:
            minDist = distance
            del closestPermutations[:]
            closestPermutations.append(permutation)
        elif distance == minDist:
            closestPermutations.append(permutation)
    diff = minDist-value
    print('MinDist='+str(minDist))
    print('Diff='+str(diff))
    #First Check if the median is actually the closest permutation
    if value == minDist:
        return closestPermutations, diff
    medians = []
    #Try every posible single inversion on the closest permutations if the
    #difference between the median and the permutation is 1
    if diff == 1:
        for permutation in closestPermutations:
            temp=permutation[:]
            for i in range(0,len(permutation)-1):
                for j in range(i+1,len(permutation)):
                    #swap
                    temp[i], temp[j] = temp[j], temp[i]
                    ktDist = KendallTauDistance(temp,permSet)
                    print('Distance between '+ str(temp) + ' and the set is '+str(ktDist))
                    if ktDist == value:
                        medians.append(list(temp))
                    #swap back to continue
                    temp[i], temp[j] = temp[j], temp[i]

    return medians, diff

def inversePermutation(permutation):
    inverse = list(range(len(permutation)))

    for i in range(0,len(permutation)):
        inverse[((permutation[i])-1)] = i

    return inverse

def KendallTauDistance(permutation, permSet):
    distance = 0
    totalDist = 0
    permA =inversePermutation(permutation)
    for p in permSet:
        permB = inversePermutation(p)

        #####################
        # COMPUTE DISTANCE  #
        #####################
        for i in range(0,len(p)-1):
            for j in range(i+1, len(permB)):
                if(((permA[i]<permA[j])and(permB[i]>permB[j])) or
                ((permA[i]>permA[j]) and (permB[i]<permB[j]))):
                    distance+=1

        totalDist += distance
        distance = 0
    return totalDist

############
#   MAIN   #
############

permSet = [[1, 8, 6, 14, 10, 7, 2, 4, 12, 3, 9, 13, 5, 11], [2, 12, 4, 11, 9, 10, 5, 14, 6, 7, 1, 13, 8, 3], [12, 1, 14, 6, 4, 8, 7, 11, 9, 10, 2, 13, 5, 3]]
value = 65

buildMedian(value,permSet)
