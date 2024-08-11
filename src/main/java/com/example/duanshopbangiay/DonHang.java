package com.example.duanshopbangiay;

public class DonHang {
    private String thongTinKhachHang;
    private String thongTinSanPham;
    private int soLuong;
    private double tongTien;
    private String trangThai;

    public DonHang(String thongTinKhachHang, String thongTinSanPham, int soLuong, double tongTien, String trangThai) {
        this.thongTinKhachHang = thongTinKhachHang;
        this.thongTinSanPham = thongTinSanPham;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getThongTinKhachHang() { return thongTinKhachHang; }
    public void setThongTinKhachHang(String thongTinKhachHang) { this.thongTinKhachHang = thongTinKhachHang; }

    public String getThongTinSanPham() { return thongTinSanPham; }
    public void setThongTinSanPham(String thongTinSanPham) { this.thongTinSanPham = thongTinSanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
