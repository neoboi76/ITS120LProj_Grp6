import { EvidenceModel } from "./evidence-model";

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
