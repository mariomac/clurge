/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package es.bsc.clurge.clopla.placement.config;

import es.bsc.clurge.clopla.domain.ClusterState;
import es.bsc.clurge.clopla.domain.ConstructionHeuristic;
import es.bsc.clurge.clopla.placement.config.localsearch.LocalSearch;
import es.bsc.clurge.clopla.CloplaEstimator;

/**
 * This class defines the configuration for the solver of the VM Placement problem.
 *
 * @author David Ortiz (david.ortiz@bsc.es)
 */
public class VmPlacementConfig {

    private final Policy policy;
    private final int timeLimitSeconds;
    private final ConstructionHeuristic constructionHeuristic;
    private final LocalSearch localSearch;
    private final boolean vmsAreFixed; // When set to true, the VMs that are already assigned to a host should not be
                                       // moved to a different one
    
    // energyModeller, priceModeller, and initialClusterState are static variables because they are needed in 
    // the score calculators and I cannot call their constructors directly. 
    // I made them ThreadLocal to make the library thread-safe. Is there a cleaner solution?
    public static ThreadLocal<CloplaEstimator> energyModeller = new ThreadLocal<>();
    public static ThreadLocal<CloplaEstimator> priceModeller = new ThreadLocal<>();
    public static ThreadLocal<ClusterState> initialClusterState = new ThreadLocal<>();

    public static class Builder {
        // Required parameters
        private final Policy policy;
        private final int timeLimitSeconds;
        private final ConstructionHeuristic constructionHeuristic;
        private final LocalSearch localSearch;
        private final boolean vmsAreFixed;

        // Optional parameters
        private CloplaEstimator energyModeller = null;
        private CloplaEstimator priceModeller = null;

        public Builder(Policy policy, int timeLimitSeconds, ConstructionHeuristic constructionHeuristic,
                LocalSearch localSearch, boolean vmsAreFixed) {
            this.policy = policy;
            this.timeLimitSeconds = timeLimitSeconds;
            this.constructionHeuristic = constructionHeuristic;
            this.localSearch = localSearch;
            this.vmsAreFixed = vmsAreFixed;
        }

        public Builder energyModeller(CloplaEstimator energyModeller) {
            this.energyModeller = energyModeller;
            return this;
        }

        public Builder priceModeller(CloplaEstimator priceModeller) {
            this.priceModeller = priceModeller;
            return this;
        }

        public VmPlacementConfig build() {
            return new VmPlacementConfig(this);
        }
    }

    private VmPlacementConfig(Builder builder) {
        policy = builder.policy;
        timeLimitSeconds = builder.timeLimitSeconds;
        constructionHeuristic = builder.constructionHeuristic;
        localSearch = builder.localSearch;
        vmsAreFixed = builder.vmsAreFixed;
        energyModeller.set(builder.energyModeller);
        priceModeller.set(builder.priceModeller);
    }

    public Policy getPolicy() {
        return policy;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public ConstructionHeuristic getConstructionHeuristic() {
        return constructionHeuristic;
    }

    public LocalSearch getLocalSearch() {
        return localSearch;
    }

    public boolean vmsAreFixed() {
        return vmsAreFixed;
    }

}
