package com.blubbax.esa.transactionManager.workload;

import org.springframework.stereotype.Service;

@Service
public class WorkloadService {

    /**
        Get primes for a given number as an exammple to generate load
     */
    public void doWork(long number) {
        if (number == 1) return;
        long t = 2;
        while (t * t <= number) {
            if (number % t == 0) {
                number /= t;
            } else t++;
        }
        return;
    }

}
