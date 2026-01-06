package org.example.processserver.pojo;

/**
 * @author PY.Lu
 * @date 2024/10/31
 * @Description
 */
public class CSVloaction {
    private Integer id;
    private String Incre_all_Loca;
    private String Init_all_Loca;
    private String Incre_custom_Loca;
    private String receive_Loca;

    public CSVloaction() {
    }

    public CSVloaction(Integer id, String incre_all_Loca, String init_all_Loca, String incre_custom_Loca, String receive_Loca) {
        this.id = id;
        Incre_all_Loca = incre_all_Loca;
        Init_all_Loca = init_all_Loca;
        Incre_custom_Loca = incre_custom_Loca;
        this.receive_Loca = receive_Loca;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIncre_all_Loca() {
        return Incre_all_Loca;
    }

    public void setIncre_all_Loca(String incre_all_Loca) {
        Incre_all_Loca = incre_all_Loca;
    }

    public String getInit_all_Loca() {
        return Init_all_Loca;
    }

    public void setInit_all_Loca(String init_all_Loca) {
        Init_all_Loca = init_all_Loca;
    }

    public String getIncre_custom_Loca() {
        return Incre_custom_Loca;
    }

    public void setIncre_custom_Loca(String incre_custom_Loca) {
        Incre_custom_Loca = incre_custom_Loca;
    }

    public String getReceive_Loca() {
        return receive_Loca;
    }

    public void setReceive_Loca(String receive_Loca) {
        this.receive_Loca = receive_Loca;
    }

    @Override
    public String toString() {
        return "CSVloaction{" +
                "id=" + id +
                ", Incre_all_Loca='" + Incre_all_Loca + '\'' +
                ", Init_all_Loca='" + Init_all_Loca + '\'' +
                ", Incre_custom_Loca='" + Incre_custom_Loca + '\'' +
                ", receive_Loca='" + receive_Loca + '\'' +
                '}';
    }
}
