import { EvidenceModel } from "./evidence-model";

export type ResultModel = {
    status: string;
    claim: string;
    verdict: string;
    reasoning: string;
    evidences: EvidenceModel;
}