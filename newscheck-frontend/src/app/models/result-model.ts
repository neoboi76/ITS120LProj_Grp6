import { EvidenceModel } from "./evidence-model";

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Model class displaying results in home

export type ResultModel = {
    status: string;
    claim: string;
    verdict: string;
    reasoning: string;
    evidences: EvidenceModel;
}