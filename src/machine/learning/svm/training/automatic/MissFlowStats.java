package machine.learning.svm.training.automatic;



 public class MissFlowStats {

        private long missFlowCount;
        private double missFlowRate;

        public MissFlowStats(long missFlowCount, double missFlowRate) {
            this.missFlowCount = missFlowCount;
            this.missFlowRate = missFlowRate;
        }

        public long getMissFlowCount() {
            return missFlowCount;
        }

        public void setMissFlowCount(long missFlowCount) {
            this.missFlowCount = missFlowCount;
        }

        public double getMissFlowRate() {
            return missFlowRate;
        }

        public void setMissFlowRate(double missFlowRate) {
            this.missFlowRate = missFlowRate;
        }

    @Override
    public String toString() {
        return "MissFlowStats{" + "missFlowCount=" + missFlowCount + ", missFlowRate=" + missFlowRate + '}';
    }
        
        

    }
