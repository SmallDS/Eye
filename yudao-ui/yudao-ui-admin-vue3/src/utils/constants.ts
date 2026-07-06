/**
 * Shared enum constants for enabled modules.
 */

// ========== COMMON ==========
export const CommonStatusEnum = {
  ENABLE: 0,
  DISABLE: 1
}

export const UserTypeEnum = {
  MEMBER: 1,
  ADMIN: 2
}

// ========== SYSTEM ==========
export const SystemMenuTypeEnum = {
  DIR: 1,
  MENU: 2,
  BUTTON: 3
}

export const SystemRoleTypeEnum = {
  SYSTEM: 1,
  CUSTOM: 2
}

export const SystemDataScopeEnum = {
  ALL: 1,
  DEPT_CUSTOM: 2,
  DEPT_ONLY: 3,
  DEPT_AND_CHILD: 4,
  DEPT_SELF: 5
}

export const SystemUserSocialTypeEnum = {
  DINGTALK: {
    title: '\u9489\u9489',
    type: 20,
    source: 'dingtalk',
    img: 'https://s1.ax1x.com/2022/05/22/OzMDRs.png'
  },
  WECHAT_ENTERPRISE: {
    title: '\u4f01\u4e1a\u5fae\u4fe1',
    type: 30,
    source: 'wechat_enterprise',
    img: 'https://s1.ax1x.com/2022/05/22/OzMrzn.png'
  }
}

export const SystemUserSexEnum = {
  UNKNOWN: 0,
  MALE: 1,
  FEMALE: 2
}

// ========== INFRA ==========
export const InfraCodegenTemplateTypeEnum = {
  CRUD: 1,
  TREE: 2,
  SUB: 15
}

export const InfraJobStatusEnum = {
  INIT: 0,
  NORMAL: 1,
  STOP: 2
}

export const InfraApiErrorLogProcessStatusEnum = {
  INIT: 0,
  DONE: 1,
  IGNORE: 2
}

// ========== AREA ==========
export const AreaLevelEnum = {
  PROVINCE: 1,
  CITY: 2,
  DISTRICT: 3
}