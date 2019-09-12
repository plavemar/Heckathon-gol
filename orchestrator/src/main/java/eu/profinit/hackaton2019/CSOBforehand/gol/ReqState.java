package eu.profinit.hackaton2019.CSOBforehand.gol;

import lombok.Data;

import java.util.List;

@Data
public class ReqState {

    private int itemsCount;
    private List<List<Boolean>> values;

}
