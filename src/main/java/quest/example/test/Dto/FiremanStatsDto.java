package quest.example.test.Dto;

public class FiremanStatsDto {
    private int firemenCount;
    private int firesCount;

    public FiremanStatsDto(int firemenCount, int firesCount) {
        this.firemenCount = firemenCount;
        this.firesCount = firesCount;
    }

    public int getFiremenCount() {
        return firemenCount;
    }
    public void setFiremenCount(int firemenCount) {
        this.firemenCount = firemenCount;
    }
    public int getFiresCount() {
        return firesCount;
    }
    public void setFiresCount(int firesCount) {
        this.firesCount = firesCount;
    }
}
