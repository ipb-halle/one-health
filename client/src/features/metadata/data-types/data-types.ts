import {
    faQuoteRight,
    faSquareCheck,
    faListNumeric,
    faCalendar,
    faList,
    IconDefinition,
} from '@fortawesome/free-solid-svg-icons';

export class DataTypes {
    public static readonly STRING = 'STRING';
    public static readonly DATE = 'DATE';
    public static readonly NUMBER = 'NUMBER';
    public static readonly BOOLEAN = 'BOOLEAN';

    public static ICONS_MAPPING() {
        var persons: { [id: string]: IconDefinition } = {};
        persons[this.STRING] = faQuoteRight;
        persons[this.DATE] = faCalendar;
        persons[this.NUMBER] = faListNumeric;
        persons[this.BOOLEAN] = faSquareCheck;
        return persons;
    }

    public static TYPE_OPTIONS() {
        return [
            { label: 'String', value: this.STRING },
            { label: 'Date', value: this.DATE },
            { label: 'Number', value: this.NUMBER },
            { label: 'Boolean', value: this.BOOLEAN },
        ];
    }
}
