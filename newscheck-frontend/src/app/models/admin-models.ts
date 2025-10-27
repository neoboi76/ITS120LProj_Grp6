export interface DashboardStats {
  totalUsers: number;
  adminUsers: number;
  regularUsers: number;
  totalVerifications: number;
  verifiedCount: number;
  pendingCount: number;
  failedCount: number;
  totalAuditLogs: number;
  verificationsLast7Days: number;
  auditLogsLast7Days: number;
  newUsersLast30Days: number;
  verdictTypeStats: {
    [key: string]: number;
  };
}

export interface UserResponse {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  gender: string;
  country: string;
  language: string;
  role: string;
  createdAt: string;
  totalVerifications: number;
  totalAuditLogs: number;
}

export interface VerificationResponse {
  verificationId: number;
  status: string;
  claim: string;
  verdictType: string;
  reasoning: string;
  confidenceScore: number;
  submittedAt: string;
  completedAt: string;
}

export interface AuditLogResponse {
  auditId: number;
  userId: number;
  userEmail: string;
  userName: string;
  action: string;
  ipAddress: string;
  userAgent: string;
  details: string;
  timestamp: string;
  verificationId: number;
  success: boolean;
  errorMessage: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  pageSize: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
