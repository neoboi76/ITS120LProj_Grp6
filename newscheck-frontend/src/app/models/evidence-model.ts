/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Model class for evidences


export type EvidenceModel = {
    evidenceId: number;
    sourceName: string;
    sourceUrl: string;
    description: string;
    relevanceScore: number;
}