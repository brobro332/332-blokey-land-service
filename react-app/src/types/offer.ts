import { OfferStatusType, OfferType } from "./common";

export interface Offer {
  id: number;
  projectId: number;
  projectTitle: string;
  blokeyId: string;
  blokeyNickname: string;
  blokeyBio: string;
  offerer: OfferType;
  status: OfferStatusType;
  createdAt: string;
}
