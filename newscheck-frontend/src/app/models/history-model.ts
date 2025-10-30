import { EvidenceModel } from "./evidence-model";
/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Model class for history page
export interface HistoryModel {
    verificationId: number;
    status: string;
    verdictType: string;
    reasoning: string;
    confidenceScore: number;
    evidences: EvidenceModel[];
    date: string;
    claim: string;
    submittedAt: string;
    completedAt: string;
    message: string;
}
