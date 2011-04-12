package acumen.map.model;

import java.util.Comparator;

/**
  * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 11:32:23
 */
class AssociationTypeRoleComparator implements Comparator<IAssociation> {

        private static AssociationTypeRoleComparator _instance;

        public static AssociationTypeRoleComparator getInstance () {
            if (_instance == null) {
                _instance = new AssociationTypeRoleComparator();
            }
            return _instance;
        }

        @Override
        public int compare(IAssociation a1, IAssociation a2) {
            int typeComparison = a1.getType().compareTo(a2.getType());
            if (typeComparison == 0) {
                return a1.getRole().compareTo(a2.getRole());
            } else {
                return typeComparison;
            }
        }

        protected AssociationTypeRoleComparator () {
            // stop instantiation
        }
}
