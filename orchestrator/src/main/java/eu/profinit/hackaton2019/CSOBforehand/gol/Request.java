package eu.profinit.hackaton2019.CSOBforehand.gol;

import lombok.Data;

@Data
public class Request {
    private String clientID;
    private ReqState state;
}
