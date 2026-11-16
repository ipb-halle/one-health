import FormulaRenderer from "@/shared/components/FormulaRenderer";

type FormularComponentProps = {
    isNP: boolean;
    formular: string | undefined;
};

export function FormularComponent({ isNP, formular }: FormularComponentProps) {
    return isNP && formular ? (
        <FormulaRenderer formula={formular} />
    ) : (
        <>{formular}</>
    );
}