package com.company;

public class SequenceReconstruction {
    public boolean sequenceReconstruction(int[] org, int[][] seqs) {
        if(seqs == null || seqs.length == 0){
            return false;
        }
        int orgLen = org.length;
        int[] idx = new int[orgLen + 1];
        boolean[] pairs = new boolean[orgLen];

        for(int i = 0; i < orgLen; i++){
            idx[org[i]] = i;
        }

        for(int[] seq : seqs){
            for(int i = 0; i < seq.length; i++){
                if(seq[i] > orgLen || seq[i] < 0){
                    return false;
                }

                if(i > 0 && idx[seq[i - 1]] >= idx[seq[i]]){
                    return false;
                }

                if(i > 0 && idx[seq[i - 1]] + 1 == idx[seq[i]]){
                    pairs[idx[seq[i - 1]]] = true;
                }
            }
        }
`
        for(int i = 0; i < orgLen - 1; i++){
            if(!pairs[i]){
                return false;
            }
        }

        return true;
    }
}
