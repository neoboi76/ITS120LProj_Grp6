import { EvidenceModel } from "./evidence-model";

export type HistoryModel = {
    map(arg0: (item: any) => { verificationId: any; status: any; verdict: any; reasoning: any; confidenceScore: any; evidences: any; date: any; news: any; }): any;
    verificationId: number;
    status: string;
    verdict: string
    reasoning: string;
    confidenceScore: number;
    evidences: EvidenceModel[];
    date: string;
    news: string;
    submittedAt: string;
    completedAt: string;
    message: string;
}