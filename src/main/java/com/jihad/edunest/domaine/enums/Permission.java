package com.jihad.edunest.domaine.enums;

public enum Permission {
    CAN_VIEW_SCHOOLS,          // Permission pour voir les écoles
    CAN_COMPARE_SCHOOLS,       // Permission pour comparer les écoles
    CAN_CONTACT_SCHOOLS,       // Permission pour contacter les écoles
    CAN_MANAGE_PROFILE,        // Permission pour gérer le profil (écoles)
    CAN_PUBLISH_OFFERS,        // Permission pour publier des offres (écoles)
    CAN_VIEW_STATISTICS,       // Permission pour voir les statistiques (écoles)
    CAN_MANAGE_USERS,          // Permission pour gérer les utilisateurs (admin)
    CAN_MANAGE_SCHOOLS,        // Permission pour gérer les écoles (admin)
    CAN_MODERATE_REVIEWS,      // Permission pour modérer les avis (admin)
    CAN_VIEW_GLOBAL_STATS;     // Permission pour voir les statistiques globales (admin)
}
