package machine.learning.svm.training.automatic;

public class StatFromTable {

        private double lookUpRate = 0;
        private double matchRate = 0;
        private double occupation_average = 0;
        private int lookUpCount = 0;
        private int matchCount = 0;

        public StatFromTable() {
        }

        public StatFromTable(double lookUpRate, double matchRate, double occupation_average, int lookUpCount, int matchCount) {
            this.lookUpRate = lookUpRate;
            this.matchRate = matchRate;
            this.occupation_average = occupation_average;
            this.lookUpCount = lookUpCount;
            this.matchCount = matchCount;
        }

        public double getLookUpRate() {
            return lookUpRate;
        }

        public void setLookUpRate(double lookUpRate) {
            this.lookUpRate = lookUpRate;
        }

        public double getMatchRate() {
            return matchRate;
        }

        public void setMatchRate(double matchRate) {
            this.matchRate = matchRate;
        }

        public double getOccupation_average() {
            return occupation_average;
        }

        public void setOccupation_average(double occupation_average) {
            this.occupation_average = occupation_average;
        }

        public int getLookUpCount() {
            return lookUpCount;
        }

        public void setLookUpCount(int lookUpCount) {
            this.lookUpCount = lookUpCount;
        }

        public int getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }

    @Override
    public String toString() {
        return "StatFromTable{" + "lookUpRate=" + lookUpRate + ", matchRate=" + matchRate + ", occupation_average=" + occupation_average + ", lookUpCount=" + lookUpCount + ", matchCount=" + matchCount + '}';
    }
        
        

    }
