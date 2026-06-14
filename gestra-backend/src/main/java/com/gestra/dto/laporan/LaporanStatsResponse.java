package com.gestra.dto.laporan;

public class LaporanStatsResponse {

    private long totalLaporan;
    private long laporanPending;
    private long laporanDiproses;
    private long laporanSelesai;
    private long totalPengguna;

    public LaporanStatsResponse() {}

    public LaporanStatsResponse(long totalLaporan, long laporanPending,
                                 long laporanDiproses, long laporanSelesai,
                                 long totalPengguna) {
        this.totalLaporan = totalLaporan;
        this.laporanPending = laporanPending;
        this.laporanDiproses = laporanDiproses;
        this.laporanSelesai = laporanSelesai;
        this.totalPengguna = totalPengguna;
    }

    public long getTotalLaporan() { return totalLaporan; }
    public void setTotalLaporan(long totalLaporan) { this.totalLaporan = totalLaporan; }

    public long getLaporanPending() { return laporanPending; }
    public void setLaporanPending(long laporanPending) { this.laporanPending = laporanPending; }

    public long getLaporanDiproses() { return laporanDiproses; }
    public void setLaporanDiproses(long laporanDiproses) { this.laporanDiproses = laporanDiproses; }

    public long getLaporanSelesai() { return laporanSelesai; }
    public void setLaporanSelesai(long laporanSelesai) { this.laporanSelesai = laporanSelesai; }

    public long getTotalPengguna() { return totalPengguna; }
    public void setTotalPengguna(long totalPengguna) { this.totalPengguna = totalPengguna; }
}
